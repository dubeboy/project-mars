package com.dubedivine.apps.yerrr

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class YerrrApplication

fun main(args: Array<String>) {
	runApplication<YerrrApplication>(*args)
}

// TODO:  add ControllerAdvisor to check errors etc and when adding The actuator