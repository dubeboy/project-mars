package com.dubedivine.apps.yerrr.repository.circle

import com.dubedivine.apps.yerrr.model.PeopleCircle
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
}