package com.dubedivine.apps.yerrr.repository.circle

import com.dubedivine.apps.yerrr.model.PeopleCircle
import com.dubedivine.apps.yerrr.model.Status
import com.dubedivine.apps.yerrr.model.User
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class CircleRepositoryImplementation(private val circlesRepository: CirclesRepository,
                                     private val template: MongoTemplate) {

    fun create(circle: PeopleCircle): PeopleCircle = circlesRepository.save(circle)

    fun findByIdOrNull(circleName : String): PeopleCircle? = circlesRepository.findByIdOrNull(circleName)


    fun joinCircle(circle: PeopleCircle, user: User): Boolean {
        val result = template.updateFirst(
                Query.query(Criteria.where("_id").isEqualTo(circle.name)),
                Update().push("users", user),
                PeopleCircle::class.java
        )
        return result.wasAcknowledged()
    }

    fun getUserCircles(user: User): List<PeopleCircle>? {
        // create a the aggregates users by Points and or slices slices
        // limit the circles to 50 and cache them on device
        val result = template.find(Query.query(Criteria.where("users.id").isEqualTo(user.id)), PeopleCircle::class.java)
        return result
    }

    fun getAllCircles(): List<PeopleCircle> {
        val result = circlesRepository.findAll()
        return result
    }

    fun getRecommendedCircles() {
//        The query is specified as a Query which can be created either using the BasicQuery or the more feature rich Query.
        TODO()
    }

    fun getStatusesForCircle(name: String, page: Int): List<Status> {
        val circle = circlesRepository.getStatusesForCircle(name, page) ?: return emptyList()
        return circle.statuses
    }

    fun addStatus(insertedStatus: Status): Boolean {
        // first chekc if this user belongs to this status else false
        // push this status
        // TODO: guard againsts a null name
        val result = template.updateFirst(
                Query.query(Criteria.where("name").isEqualTo(insertedStatus.circleName)
                ),
                Update().push("statuses", insertedStatus),
                PeopleCircle::class.java
        )
        return result.wasAcknowledged()
    }

    fun deleteStatus(status: Status) {
        TODO("This hsould delete the the status from this repository as well")
    }

    fun deleteUser(user: User) {

    }
}