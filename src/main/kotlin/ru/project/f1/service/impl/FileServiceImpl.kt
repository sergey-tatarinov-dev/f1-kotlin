package ru.project.f1.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import ru.project.f1.entity.F1File
import ru.project.f1.repository.FileRepository
import ru.project.f1.service.FileService
import java.io.File
import java.math.BigInteger
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

@Service
class FileServiceImpl : FileService {

    @Value("\${f1.file-storage.base-dir}")
    private lateinit var fileStorageRootPath: String

    @Autowired
    private lateinit var fileRepository: FileRepository

    override fun save(file: File, fileName: String, fileContent: ByteArray): F1File {
        val f1File = F1File()
        f1File.apply {
            val split = file.path.split(".")
            name = fileName
            extension = split[1]
            fullPath = file.path
        }
        val savedFile = fileRepository.save(f1File)
        Files.write(Paths.get(fileStorageRootPath + file.path), fileContent)
        return savedFile
    }

    override fun findById(id: BigInteger): Optional<File> {
        val f1File = fileRepository.findById(id)
        return f1File.map { File(fileStorageRootPath + it.fullPath) }
    }

    override fun deleteById(id: BigInteger) = fileRepository.deleteById(id)

    override fun findAll(pageable: Pageable): Page<F1File> = fileRepository.findAll(pageable)

}