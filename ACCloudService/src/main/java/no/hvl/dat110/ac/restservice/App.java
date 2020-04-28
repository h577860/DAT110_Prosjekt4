package no.hvl.dat110.ac.restservice;

import com.google.gson.Gson;

import static spark.Spark.*;

/**
 * Hello world!
 *
 */
public class App {
	
	static AccessLog accesslog = null;
	static AccessCode accesscode = null;
	
	public static void main(String[] args) {

		if (args.length > 0) {
			port(Integer.parseInt(args[0]));
		} else {
			port(8080);
		}

		// objects for data stored in the service
		
		accesslog = new AccessLog();
		accesscode  = new AccessCode();
		
		after((req, res) -> {
  		  res.type("application/json");
  		});
		
		// for basic testing purposes
		get("/accessdevice/hello", (req, res) -> {
			
		 	Gson gson = new Gson();
		 	
		 	return gson.toJson("IoT Access Control Device");
		});
		
		// TODO: implement the routes required for the access control service
		// as per the HTTP/REST operations describined in the project description

		//store a log message
		post("/accessdevice/log", (req, res) -> {
		Gson gson = new Gson();
		AccessMessage body= gson.fromJson(req.body(), AccessMessage.class);
		int id =accesslog.add(body.getMessage());
		AccessEntry accessEntry = accesslog.get(id);
		res.body(gson.toJson(accessEntry));

		return gson.toJson(accessEntry);
		});

		//return json representation of all entries in the accesss log
		get("/accessdevice/log", (req, res) -> accesslog.toJson());

		//return access log entry with the given {id}
		get("/accessdevice/log/:id", (req, res) -> {
			Gson gson =new Gson();
			Integer id = Integer.parseInt(req.params(":id"));
			AccessEntry accessEntry = accesslog.get(id);
			return gson.toJson(accessEntry);
		});

		//update the access code to a new combination
		put("/accessdevice/code", (req, res) -> {
			Gson gson = new Gson();
			AccessCode newAccessCode = gson.fromJson(req.body(), AccessCode.class);
			accesscode.setAccesscode(newAccessCode.getAccesscode());
			res.body(gson.toJson(newAccessCode.getAccesscode()));
			return gson.toJson(newAccessCode.getAccesscode());
		});

		// return json representation of the current access code
		get("/accessdevice/code", (req, res) -> {
			Gson gson = new Gson();
			res.body(gson.toJson(accesscode));
			return gson.toJson(accesscode);
		});

		//delete all entries in the access log and return the empty as a json element
		delete("/accessdevice/log/", (req, res) -> {
			accesslog.clear();
			return accesslog.toJson();
		});
    }
    
}
