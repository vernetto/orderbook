# orderbook


H2 console: http://localhost:8080/h2-console/ connect as Generic H2 (Embedded), org.h2.Driver, jdbc:h2:mem:orders, sa/password

Swagger: http://localhost:8080/v2/api-docs  http://localhost:8080/swagger-ui/index.html#/order-controller


# Remarks

Lombok has regrettably not been used because not "production ready" - to be verified.

Validation has been skipped for brevity

Security also skipped for ease of demoing

In the DB the validation of enumerated columns is missing - it should be implemented by triggers of by CHECK constraint

Builders not use for brevity  

# References

https://spring.io/guides/tutorials/rest/
https://spring.io/guides/gs/testing-web/
https://www.springboottutorial.com/spring-boot-swagger-documentation-for-rest-services



# TODO

For RestController, for each @RequestMapping:
@ApiOperation(value = "Get Users ", response = Iterable.class, tags = "getUsers")
@ApiResponses(value = { @ApiResponse(code = 200, message = "Success|OK"), @ApiResponse(code = 401, message = "Not Authorized!"),
@ApiResponse(code = 403, message = "Forbidden!"), @ApiResponse(code = 404, message = "Not Found!") })

foe Entities, for each column add
@ApiModelProperty(notes = "User Id",name="id",required=true,value="1")

CHECK constraint in DB

in RestController: 
@ResponseStatus(HttpStatus.CREATED)
@Valid 