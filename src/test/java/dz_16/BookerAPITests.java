package dz_16;

import dz_16.models.*;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;

public class BookerAPITests {

    private int firstBooking;
    private int secondBooking;

    @BeforeTest
    public void setUp() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType("application/json")
                .build();

        AuthRequest authRequest = AuthRequest.builder()
                .username("admin")
                .password("password123")
                .build();

        Response authResponse = RestAssured
                .given()
                .body(authRequest)
                .post("/auth");
        String token = authResponse.as(AuthResponse.class).getToken();

        RestAssured.requestSpecification = new RequestSpecBuilder()
                .addCookie("token",token)
                .build();
    }

    @Test(testName = "POST")
    public void createNewBooking(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate checkinDate = LocalDate.of(2024, 1, 1);
        LocalDate checkoutDate = LocalDate.of(2024, 1, 10);

        BookingDatesBody bookingDates = BookingDatesBody.builder()
                .checkin(checkinDate.format(formatter))
                .checkout(checkoutDate.format(formatter))
                .build();

        FullBookingBodyRequest body = FullBookingBodyRequest.builder()
                .firstname("Paul")
                .lastname("May")
                .totalprice(319)
                .depositpaid(true)
                .bookingdates(bookingDates)
                .additionalneeds("Water")
                .build();

        Response createResponse = RestAssured
                .given()
                .body(body)
                .post("/booking");
        createResponse.then().statusCode(200);
        createResponse.prettyPrint();
    }

    @BeforeClass()
    @Test(testName = "GET")
    public void getAllBooking(){
        Response response = given()
                .get("/booking");
        response.then().statusCode(200);

        JSONArray responceContent = new JSONArray(response.asString());
        firstBooking = responceContent.getJSONObject(0).getInt("bookingid");
        secondBooking = responceContent.getJSONObject(1).getInt("bookingid");
    }

    @Test(testName = "PATCH")
    public void partialUpdateBooking(){
        PatchBookingRequest updateBody = PatchBookingRequest.builder()
                .totalPrice(183)
                .build();

        Response updateResponse = RestAssured
                .given()
                .body(updateBody)
                .patch("/booking/{id}",firstBooking);

        updateResponse.then().statusCode(200);
        updateResponse.prettyPrint();
    }

    @Test (testName = "PUT")
    public void updateBooking(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate checkinDate = LocalDate.of(2024, 1, 1);
        LocalDate checkoutDate = LocalDate.of(2024, 1, 10);

        BookingDatesBody bookingDates = BookingDatesBody.builder()
                .checkin(checkinDate.format(formatter))
                .checkout(checkoutDate.format(formatter))
                .build();

        FullBookingBodyRequest body = FullBookingBodyRequest.builder()
                .firstname("Paul")
                .lastname("May")
                .totalprice(319)
                .depositpaid(true)
                .bookingdates(bookingDates)
                .additionalneeds("Water")
                .build();

        Response updateResponse = RestAssured
                .given()
                .body(body)
                .put("/booking/{id}",secondBooking);

        updateResponse.then().statusCode(200);
        updateResponse.prettyPrint();
    }

    @Test(testName = "DELETE")
    public void deleteBooking(){
        Response updateResponse = RestAssured
                .delete("/booking/{id}",firstBooking);

        updateResponse.then().statusCode(201);
        updateResponse.prettyPrint();
    }
}
