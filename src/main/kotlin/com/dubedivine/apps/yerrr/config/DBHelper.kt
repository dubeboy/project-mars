package com.dubedivine.apps.yerrr.config

import com.dubedivine.apps.yerrr.repository.status.StatusRepository
import com.dubedivine.apps.yerrr.model.Status
import com.dubedivine.apps.yerrr.model.User
import com.dubedivine.apps.yerrr.repository.status.StatusVoteRepository
import com.dubedivine.apps.yerrr.repository.UserRepository
import com.dubedivine.apps.yerrr.repository.status.StatusLikeRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.data.mongodb.core.query.Query
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
               private val gridFsOperations: GridFsOperations,
               private val likeRepostory: StatusLikeRepository,
               private val userRepository: UserRepository): CommandLineRunner {
    override fun run(vararg args: String?) {
        println("💣 Deleting everything....")
        repository.deleteAll()
        statusCommentRepository.deleteAll()
        gridFsOperations.delete(Query())
        voteRepository.deleteAll()
        userRepository.deleteAll()
        likeRepostory.deleteAll()
        println("💣 Done \uD83D\uDCA3")

        val user = userRepository.save(User("1000", "Booty", "@bootynizer", "0891231234", null))

        val statuses = arrayListOf(
                Status("""Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been th
                            |e industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of
                            | type and scrambled it to make a type specimen book.""".trimMargin(), id = "5f309adb3dd75b62ddacf344", user = user), // THis one is for testing
                Status("""Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been th
                            | type and scrambled it to make a type specimen book.""".trimMargin(), user),
                Status("""Lorem Ipsum has been th
                            |s opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages
                            |and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover
                            |many web sites still in their infancy. Various versions have evolved
                            |over the years, sometimes by acc
                            | type and scrambled it to make a type specimen book.""".trimMargin(), user),
                Status("""Lorem Ipsum has been th
                            | type and scrambled it to make a type specimen book.""".trimMargin(), user),
                Status("""Lorem Ipsum has been th
                            | type and scrambled it to make a type specimen book.""".trimMargin(), user),
                Status("""Lorem Ipsum has been th
                            |s opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages
                            |and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover
                            |many web sites still in their infancy. Various versions have evolved
                            |over the years, sometimes by acc
                            | type and scrambled it to make a type specimen book.""".trimMargin(), user),
                Status("""Lorem Ipsum has been th
                            |s opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages
                            |and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover
                            |many web sites still in their infancy. Various versions have evolved
                            |over the years, sometimes by acc
                            | type and scrambled it to make a type specimen book.""".trimMargin(), user),
                Status("""Lorem Ipsum has been th
                            |s opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages
                            |and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover
                            |many web sites still in their infancy. Various versions have evolved
                            |over the years, sometimes by acc
                            | type and scrambled it to make a type specimen book.""".trimMargin(), user),
                Status("""Lorem Ipsum has been th
                            |s opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages
                            |and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover
                            |many web sites still in their infancy. Various versions have evolved
                            |over the years, sometimes by acc
                            | type and scrambled it to make a type specimen book.""".trimMargin(), user)
        )

        println("🚀 Seeding data....")
        repository.saveAll(statuses)
        println("🚀 Done \uD83D\uDE80")
    }

}