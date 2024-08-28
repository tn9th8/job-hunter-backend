plugins {
	java
	id("org.springframework.boot") version "3.2.4"
	id("io.spring.dependency-management") version "1.1.4"
  	id("io.freefair.lombok") version "8.6"
}

group = "vn.nhannt"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator") // check health of the system
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6") // use it for sending mail
	implementation("com.turkraft.springfilter:jpa:3.1.7")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("com.mysql:mysql-connector-j") // j is  short-hand of java
	testImplementation("org.springframework.boot:spring-boot-starter-test") // use it for writing test cases
	testImplementation("org.springframework.security:spring-security-test")
	
}

tasks.withType<Test> {
	useJUnitPlatform()
}
