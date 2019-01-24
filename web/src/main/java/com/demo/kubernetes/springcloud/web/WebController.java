package com.demo.kubernetes.springcloud.web;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class WebController {

	@Autowired
	protected WebService service;

	protected Logger logger = Logger.getLogger(WebController.class
			.getName());

	@Autowired
	public WebController(WebService service) {
		logger.info("WebController initiated");
		;
		this.service = service;
	}

	@RequestMapping(value = "/zip/getZipcodeInfo/{zipcode}", produces = {"text/html"})
	public String zipInfo(@PathVariable("zipcode") String zipcode) {

		String response = service.getZipInfo(zipcode);
//		logger.info(response);
		JsonNode root = null;

		try {
			// JACKSON
			ObjectMapper mapper = new ObjectMapper();

			// GET JSON OUT OF RESPONSE BODY
			root = mapper.readTree(response);

			logger.info("============");
			logger.info(root.toString());
			logger.info("============");

		} catch (IOException e) {
			e.printStackTrace();
		}


		StringBuilder result = new StringBuilder();
		result.append("<html><body>");
		if (root != null) {
			result.append("<h1>");
			result.append(root.get("places").elements().next().get("place name"));
			result.append("</h1");
			result.append(root.toString());
		}
		result.append("</body></html>");
		return result.toString();
	}
	
/*
{
	"post code": "33301",
	"country": "United States",
	"country abbreviation": "US",
	"places": [
				{
				"place name": "Fort Lauderdale",
				"longitude": "-80.1288",
				"state": "Florida",
				"state abbreviation": "FL",
				"latitude": "26.1216"
				}
			]
}
 */


	class ZipCode {
		@JsonProperty("post code")
		String post_code;
		@JsonProperty("country abbreviation")
		String country_abbr;
		String country;
		@JsonDeserialize(using = PlacesJsonDeserializer.class)
		List<Place> places;

		public ZipCode() {
		}

		public String getPost_code() {
			return post_code;
		}

		public void setPost_code(String post_code) {
			this.post_code = post_code;
		}

		public String getCountry_abbr() {
			return country_abbr;
		}

		public void setCountry_abbr(String country_abbr) {
			this.country_abbr = country_abbr;
		}

		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}

		public List<Place> getPlaces() {
			return places;
		}

		public void setPlaces(List<Place> places) {
			this.places = places;
		}




		public String toString() {
			StringBuilder strBldr = new StringBuilder();
			strBldr.append("<p>Zipcode Information:<p>zip: " + post_code +
					", Country: " + country + ", Country Abbr: " + country_abbr);

			return strBldr.toString();
		}
	}

	class Place {
		@JsonProperty("place name")
		String place_name;
		@JsonProperty("state abbreviation")
		String country_abbr;
		String longitude;
		String latitude;
		String state;

		public Place() {
		}

		public String getPlace_name() {
			return place_name;
		}

		public void setPlace_name(String place_name) {
			this.place_name = place_name;
		}

		public String getCountry_abbr() {
			return country_abbr;
		}

		public void setCountry_abbr(String country_abbr) {
			this.country_abbr = country_abbr;
		}

		public String getLongitude() {
			return longitude;
		}

		public void setLongitude(String longitude) {
			this.longitude = longitude;
		}

		public String getLatitude() {
			return latitude;
		}

		public void setLatitude(String latitude) {
			this.latitude = latitude;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String toString() {
			StringBuilder strBldr = new StringBuilder();
			strBldr.append("<p>Place:<p>zip: " + place_name +
					", State: " + country_abbr);

			return strBldr.toString();
		}
	}




}


class PlacesJsonDeserializer extends JsonDeserializer<List<WebController.Place>> {

	@Override
	public List<WebController.Place> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		InnerItems innerItems = jp.readValueAs(InnerItems.class);

		return innerItems.elements;
	}

	private static class InnerItems {
		public List<WebController.Place> elements;
	}
}



