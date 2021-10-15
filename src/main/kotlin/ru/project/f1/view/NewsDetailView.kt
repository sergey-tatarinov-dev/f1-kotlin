package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.*
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.PreserveOnRefresh
import com.vaadin.flow.router.Route
import com.vaadin.flow.spring.annotation.UIScope
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import ru.project.f1.entity.Comment
import ru.project.f1.entity.News
import ru.project.f1.service.CommentService
import ru.project.f1.service.NewsService
import ru.project.f1.utils.SecurityUtils.Companion.getUser
import ru.project.f1.utils.SecurityUtils.Companion.isUserLoggedIn
import ru.project.f1.utils.UiUtils.Companion.customDialog
import ru.project.f1.utils.UiUtils.Companion.setLocation
import ru.project.f1.utils.UiUtils.Companion.show
import ru.project.f1.utils.Utils.Companion.format
import ru.project.f1.view.fragment.HeaderBarFragment.Companion.headerBar
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Route("news/read/:id")
@Component
@PreserveOnRefresh
@UIScope
class NewsDetailView : KComposite(), BeforeEnterObserver {

    @Autowired
    private lateinit var newsService: NewsService

    @Autowired
    private lateinit var commentService: CommentService
    private lateinit var newId: String
    private lateinit var title: H1
    private lateinit var timeSpan: Span
    private lateinit var newsBlock: Div
    private lateinit var commentsBlock: Div
    private lateinit var commentsTitle: H3
    private lateinit var commentsField: TextField
    private lateinit var publishButton: Button
    private lateinit var news: News
    private var selectedComment: Comment? = null
    private var formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)!!

    val root = ui {
        verticalLayout {
            headerBar { }
            setSizeFull()
            verticalLayout {
                alignSelf = FlexComponent.Alignment.CENTER
                width = "65%"

                horizontalLayout {
                    width = "100%"
                    title = h1 { width = "100%" }
                    timeSpan = span { width = "15%" }
                }
                newsBlock = div { }
                commentsTitle = h3 { }
                commentsBlock = div { setWidthFull() }
                commentsField = textField {
                    width = "100%"
                    placeholder = "Enter your comment"
                    isEnabled = isUserLoggedIn()
                    addValueChangeListener {
                        publishButton.isEnabled = it.value.isNotEmpty()
                    }
                }
                publishButton = button("Publish") {
                    alignSelf = FlexComponent.Alignment.END
                    isEnabled = isUserLoggedIn()
                    setPrimary()
                    onLeftClick {
                        if (isUserLoggedIn()) {
                            val commentText = commentsField.value
                            if (selectedComment != null) {
                                selectedComment!!.text = commentText
                                commentService.save(selectedComment!!)
                                selectedComment = null
                                commentsField.clear()
                                setLocation("/news/read/${newId}")
                            } else if (commentText.isNotEmpty()) {
                                commentService.save(Comment(text = commentText, author = getUser(), news = news))
                                commentsField.clear()
                                isEnabled = false
                                setLocation("/news/read/${newId}")
                            } else {
                                show("Comment cannot be empty")
                            }
                        } else {
                            setLocation("/login")
                        }
                    }
                }

            }
        }
    }

    override fun beforeEnter(event: BeforeEnterEvent?) {
        newId = event!!.routeParameters["id"].get()
    }

    override fun onAttach(attachEvent: AttachEvent?) {
        super.onAttach(attachEvent)
        if (newId.isNotEmpty()) {
            val newsOpt = newsService.findById(newId.toBigInteger())
            newsOpt.ifPresent {
                title.text = it.title
                newsBlock.removeAll()
                Div()
                newsBlock.add(Html("<div><p>${it.text.replace("\n", "</p><p>")}</p></div>"))
                timeSpan.text = it.createdDate.format(formatter)
                news = it
                commentsTitle.apply {
                    text = "Comments (${commentService.countAllByNews(it)})"
                    style.set("line-height", "0.5")
                }
                commentsBlock.removeAll()
                commentService.findAllByNews(it, PageRequest.of(0, 50))
                    .sortedBy(Comment::createdDate)
                    .forEach(this::addComment)
            }
        }
    }

    private fun addComment(comment: Comment) {
        commentsBlock.apply {
            add(
                horizontalLayout {
                    setWidthFull()
                    add(
                        horizontalLayout {
                            setWidthFull()
                            add(
                                createParagraph(comment.author.login).apply {
                                    style.set("font-weight", "500")
                                    style.set("margin-bottom", "0px")
                                    width = "15%"
                                }
                            )
                        },
                        horizontalLayout {
                            if (isUserLoggedIn() && getUser() == comment.author) {
                                add(
                                    createButton(VaadinIcon.PENCIL) {
                                        selectedComment = comment
                                        commentsField.apply {
                                            value = comment.text
                                            isEnabled = true
                                            focus()
                                        }
                                        publishButton.apply {
                                            text = "Edit"
                                            isEnabled = true
                                        }

                                    },
                                    createButton(VaadinIcon.CLOSE) {
                                        customDialog("Are you sure you want to delete the comment?") {
                                            commentService.deleteById(comment.id)
                                            setLocation("/news/read/${newId}")
                                        }
                                    })
                            }
                        })
                },
                createParagraph(comment.text),
                createParagraph(format(comment.createdDate)).apply {
                    style.set("font-size", "13px")
                },
                Html("<br>")
            )
        }
    }

    private fun createParagraph(value: String): Paragraph {
        return Paragraph(value).apply {
            style.set("font-size", "18px")
            style.set("line-height", "0.5")
            setWidthFull()
        }
    }

    private fun createButton(vaadinIcon: VaadinIcon, block: Icon.() -> Unit): Icon {
        return Icon(vaadinIcon).apply {
            style.set("height", "14px")
            style.set("padding-top", "7px")
            style.set("cursor", "pointer")
            addClickListener {
                block()
            }
        }
    }

}