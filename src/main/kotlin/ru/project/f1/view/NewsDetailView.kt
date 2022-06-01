package ru.project.f1.view

import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.kaributools.setPrimary
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.*
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
import ru.project.f1.utils.UiUtils.Companion.failBox
import ru.project.f1.utils.UiUtils.Companion.reload
import ru.project.f1.utils.UiUtils.Companion.setLocation
import ru.project.f1.utils.Utils.Companion.toFormatted
import ru.project.f1.view.fragment.Components.Companion.createEditButton
import ru.project.f1.view.fragment.Components.Companion.createParagraph
import ru.project.f1.view.fragment.Components.Companion.createRemoveButton
import ru.project.f1.view.fragment.HeaderBarFragment.Companion.headerBar
import ru.project.f1.view.fragment.HeaderBarFragment.Companion.setError
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Route("news/read/:id")
@Component
@PreserveOnRefresh
@UIScope
class NewsDetailView : HasImage(), BeforeEnterObserver, HasDynamicTitle {

    @Autowired
    private lateinit var newsService: NewsService

    @Autowired
    private lateinit var commentService: CommentService
    private lateinit var newId: String
    private lateinit var pageTitle: String
    private lateinit var title: H1
    private lateinit var timeSpan: Span
    private lateinit var newsBlock: Div
    private lateinit var commentsBlock: Div
    private lateinit var commentsTitle: H3
    private lateinit var commentsField: TextField
    private lateinit var publishCommentButton: Button
    private lateinit var news: News
    private var publishNewsButton: Button = Button().apply { isVisible = false }
    private var refuseButton: Button = Button().apply { isVisible = false }
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
                    setWidthFull()
                    title = h1 { setWidthFull() }
                    timeSpan = span { width = "15%" }
                }
                newsBlock = div { }

                commentsTitle = h3 { }
                commentsBlock = div { setWidthFull() }
                commentsField = textField {
                    setWidthFull()
                    placeholder = "Enter your comment"
                    isEnabled = isUserLoggedIn()
                    addValueChangeListener {
                        publishCommentButton.isEnabled = it.value.isNotEmpty()
                    }
                }
                publishCommentButton = button("Publish") {
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
                                reload()
                            } else if (commentText.isNotEmpty()) {
                                commentService.save(Comment(text = commentText, author = getUser(), news = news))
                                commentsField.clear()
                                isEnabled = false
                                reload()
                            } else {
                                failBox("Comment cannot be empty")
                            }
                        } else {
                            setLocation("/login")
                        }
                    }
                }

                horizontalLayout {
                    alignSelf = FlexComponent.Alignment.END
                    refuseButton = button("Refuse") {
                        setError()
                        onLeftClick {
                            newsService.refuse(newId.toBigInteger())
                            setLocation("/news${if (newsService.countAllBySuggested(true) > 0) "/suggested" else ""}")
                        }
                    }
                    publishNewsButton = button("Publish").apply {
                        isVisible = true
                        setPrimary()
                        onLeftClick {
                            newsService.publish(newId.toBigInteger())
                            setLocation("/news${if (newsService.countAllBySuggested(true) > 0) "/suggested" else ""}")
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
                pageTitle = news.title
                listOf(commentsTitle, commentsBlock, commentsField, publishCommentButton)
                    .forEach { component -> component.apply { isVisible = !news.suggested } }
                listOf(refuseButton, publishNewsButton)
                    .forEach { component -> component.apply { isVisible = news.suggested } }
                if (!news.suggested) {
                    commentsTitle.apply {
                        text = "Comments (${commentService.countAllByNews(it)})"
                        style.set("line-height", "0.5")
                    }
                    commentsBlock.removeAll()
                    commentService.findAllByNews(it, PageRequest.of(0, 50))
                        .sortedBy(Comment::createdDate)
                        .forEach(this::addComment)
                } else {
                    if (!isUserLoggedIn()) {
                        setLocation("/news")
                    }
                }
            }
        }
    }

    private fun addComment(comment: Comment) {
        commentsBlock.apply {
            val user = comment.author
            add(
                horizontalLayout {
                    alignItems = FlexComponent.Alignment.START
                    add(
                        avatarById(user.userPic?.id!!, user.login) {
                            height = "30px"
                            width = "30px"
                            style.set("padding-top", "10px")
                        },
                        verticalLayout {
                            add(
                                createParagraph(user.login).apply {
                                    style.apply {
                                        set("font-weight", "500")
                                        set("margin-bottom", "0px")
                                    }
                                    width = "15%"
                                },
                                createParagraph(comment.text),
                                createParagraph(comment.createdDate.toFormatted()).apply {
                                    style.set("font-size", "13px")
                                }
                            )
                        },
                        horizontalLayout {
                            if (isUserLoggedIn() && getUser() == user) {
                                add(
                                    createEditButton {
                                        selectedComment = comment
                                        commentsField.apply {
                                            value = comment.text
                                            isEnabled = true
                                            focus()
                                        }
                                        publishCommentButton.apply {
                                            text = "Edit"
                                            isEnabled = true
                                        }
                                    },
                                    createRemoveButton {
                                        customDialog("Are you sure you want to delete the comment?") {
                                            commentService.deleteById(comment.id)
                                            reload()
                                        }
                                    }
                                )
                            }
                        }
                    )
                }
            )
        }
    }

    override fun getPageTitle(): String = pageTitle

}