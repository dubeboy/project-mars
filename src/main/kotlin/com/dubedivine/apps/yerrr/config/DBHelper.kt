package com.dubedivine.apps.yerrr.config

import com.dubedivine.apps.yerrr.repository.StatusRepository
import com.dubedivine.apps.yerrr.model.Status
import com.dubedivine.apps.yerrr.repository.StatusVoteRepository
import com.dubedivine.apps.yerrr.repository.shared.VoteRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.where
import org.springframework.data.mongodb.gridfs.GridFsOperations
import org.springframework.stereotype.Component
/**
 * This is a seed class that creates constant so that we can easily test or api
 * we can alyways put more data if we need to
* */
@Component
class DBHelper(private val repository: StatusRepository,
               private val voteRepository: StatusVoteRepository,
               private val statusCommentRepository: StatusVoteRepository,
               private val gridFsOperations: GridFsOperations): CommandLineRunner {
    override fun run(vararg args: String?) {
        println("💣 Deleting everything....")
        repository.deleteAll()
        statusCommentRepository.deleteAll()
        gridFsOperations.delete(Query())
        voteRepository.deleteAll()
        println("💣 Done \uD83D\uDCA3")

        val statuses = arrayListOf(
                Status("""Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been th
                            |e industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of
                            | type and scrambled it to make a type specimen book.""".trimMargin(), id = "5f309adb3dd75b62ddacf344"), // THis one is for testing
                Status("""Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been th
                            | type and scrambled it to make a type specimen book.""".trimMargin()),
                Status("""Lorem Ipsum has been th
                            |s opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages 
                            |and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover 
                            |many web sites still in their infancy. Various versions have evolved 
                            |over the years, sometimes by acc
                            | type and scrambled it to make a type specimen book.""".trimMargin()),
                Status("""Lorem Ipsum has been th
                            | type and scrambled it to make a type specimen book.""".trimMargin()),
                Status("""Lorem Ipsum has been th
                            | type and scrambled it to make a type specimen book.""".trimMargin()),
                Status("""Lorem Ipsum has been th
                            |s opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages 
                            |and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover 
                            |many web sites still in their infancy. Various versions have evolved 
                            |over the years, sometimes by acc
                            | type and scrambled it to make a type specimen book.""".trimMargin()),
                Status("""Lorem Ipsum has been th
                            |s opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages 
                            |and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover 
                            |many web sites still in their infancy. Various versions have evolved 
                            |over the years, sometimes by acc
                            | type and scrambled it to make a type specimen book.""".trimMargin()),
                Status("""Lorem Ipsum has been th
                            |s opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages 
                            |and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover 
                            |many web sites still in their infancy. Various versions have evolved 
                            |over the years, sometimes by acc
                            | type and scrambled it to make a type specimen book.""".trimMargin()),
                Status("""Lorem Ipsum has been th
                            |s opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages 
                            |and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover 
                            |many web sites still in their infancy. Various versions have evolved 
                            |over the years, sometimes by acc
                            | type and scrambled it to make a type specimen book.""".trimMargin())
        )

        println("🚀 Seeding data....")
        repository.saveAll(statuses)
        println("🚀 Done \uD83D\uDE80")
    }

}