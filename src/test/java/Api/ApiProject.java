package Api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class ApiProject {

    @Test

    public void api() {
     String res=   given().log().all().when().get("https://reqres.in/api/users?page=2").then().log().all().statusCode(200).extract().response().asString();
        System.out.println(res);





    }
    @Test(dependsOnMethods = {"api"})
    public void singleuser(){
  Response response= RestAssured.given().log().all().when().get("https://reqres.in/api/users/2").then().log().all().extract().response();
//  response.getBody();
        Assert.assertTrue(response.statusCode()==200);
        JsonPath json = response.jsonPath();
        System.out.println(json.get("data.email"));
        System.out.println(json.get("support.text"));

    }

    @Test(dependsOnMethods = {"singleuser"})
    public void usernotfound(){
        Response response = RestAssured.given().log().all().when().get("https://reqres.in//api/users/23").then().log().all().extract().response();
        Assert.assertTrue(response.statusCode()==404);
        System.out.println(response.getBody());

    }
    @Test(dependsOnMethods = {"usernotfound"})
    public void list(){
        Response response = RestAssured.given().log().all().when().get("https://reqres.in/api/unknown").then().log().all().extract().response();
        Assert.assertTrue(response.statusCode()==200);
      //  System.out.println(response.getBody());
        JsonPath js = response.jsonPath();
        System.out.println(js.get("data[0].name"));

    }

    @Test(dependsOnMethods = {"list"})
    public void createuser(){

        Response response = RestAssured.given().log().all().body(Data.data()).when().post("https://reqres.in/api/users").then().log().all().extract().response();
        Assert.assertTrue(response.statusCode()==201);
        //  System.out.println(response.getBody());
        JsonPath js = response.jsonPath();
        String id =  js.get("id");
      //
      //update

        Response response2 = RestAssured.given().log().all().pathParam("key",id).body(Data.update()).when().put("https://reqres.in/api/users/{key}").then().log().all().extract().response();
        Assert.assertTrue(response2.statusCode()==200);
        //  System.out.println(response.getBody());
        JsonPath js1 = response2.jsonPath();

//delete
        Response delete = RestAssured.given().log().all().pathParam("key",id).when().delete("https://reqres.in/api/users/{key}").then().log().all().extract().response();
        Assert.assertTrue(delete.statusCode()==204);

    }



}