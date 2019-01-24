package com.demo.kubernetes.springcloud.web;

import java.util.logging.Logger;

import com.google.gson.annotations.SerializedName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;


@RestController
public class WebController {

	@Autowired
	protected WebService service;
	
	protected Logger logger = Logger.getLogger(WebController.class
			.getName());

	@Autowired
	public WebController(WebService service) {
		logger.info("WebController initiated");;
		this.service = service;
	}
	
	@RequestMapping(value = "/zip/getZipcodeInfo/{zipcode}", produces = { "text/html" })
	public String zipInfo(@PathVariable("zipcode") String zipcode) {
		Gson gson = new Gson();
		String response = service.getZipInfo(zipcode);
		logger.info(response);
		ZipCodeInfo info = gson.fromJson(response,ZipCodeInfo.class);

		logger.info("============");
		logger.info(info.zip_code);
		StringBuilder result = new StringBuilder();
		result.append("<html><body>");
		if(info != null){
			result.append(info.toString());
		}
		result.append("</body></html>");
		return result.toString();
	}
	
	@RequestMapping(value = "/zip/getNearbyZipcodes/{zipcode}/{distance}", produces = { "text/html" })
	public String zipDistance(@PathVariable("zipcode") String zipcode,@PathVariable("distance") String distance) {
		Gson gson = new Gson();
		String response = service.getNearbyZipcodesWithinDistance(zipcode, distance);
		logger.info(response);
		ZipCodeWrapper zipCodes = gson.fromJson(response,ZipCodeWrapper.class);
		StringBuilder result = new StringBuilder();
		result.append("<html><body>");
		if(zipCodes != null){
			result.append(zipCodes.toString());
		}
		result.append("</body></html>");
		return result.toString();
	}
}

class ZipCodeWrapper{
	ZipCode[] zip_codes;

	public ZipCode[] getZip_codes() {
		return zip_codes;
	}

	public void setZip_codes(ZipCode[] zip_codes) {
		this.zip_codes = zip_codes;
	}

	public String toString() {
		StringBuilder strBldr = new StringBuilder();
		strBldr.append("<p>Zip codes:<br>"); 
		for(ZipCode zipCode:zip_codes){
			strBldr.append(zipCode);
		}
		return strBldr.toString();
	}
}

class ZipCode {
    String zip_code;
	String distance;
	String city;
	String state;
	public String getZip_code() {
		return zip_code;
	}
	public void setZip_code(String zip_code) {
		this.zip_code = zip_code;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	public String toString() {
		return "<p>zip_code=" + zip_code + ", distance=" + distance + " miles, city=" + city + ", state=" + state;
	}
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



class ZipCodeInfo {
    @SerializedName("post code") String zip_code;
	String country;
    @SerializedName("country abbreviation") String country_abbr;
//	String city;
//	String state;
//	CityState[] places;
//	Timezone timezone;


    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry_abbr() {
        return country_abbr;
    }

    public void setCountry_abbr(String country_abbr) {
        this.country_abbr = country_abbr;
    }

    class Timezone{
		String timezone_identifier;
		String timezone_abbr;
		public String getTimezone_identifier() {
			return timezone_identifier;
		}
		public void setTimezone_identifier(String timezone_identifier) {
			this.timezone_identifier = timezone_identifier;
		}
		public String getTimezone_abbr() {
			return timezone_abbr;
		}
		public void setTimezone_abbr(String timezone_abbr) {
			this.timezone_abbr = timezone_abbr;
		}
		
		public String toString() {
			return "<p>Timezone: " + timezone_identifier + " (" + timezone_abbr + ")";
		}
	}
	
	class CityState{
		String city;
		String state;
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		
		public String toString() {
			return "<p>City: " + city + ", State: " + state;
		}	
	}

	
	public String toString() {
		StringBuilder strBldr = new StringBuilder();
		strBldr.append("<p>Zipcode Information:<p>zip: " + zip_code +
                ", Country: " + country + ", Country Abbr: " + country_abbr);
//		strBldr.append(timezone);
//		strBldr.append("<p>Acceptable City Names:");
//		if((this.acceptable_city_names != null)) {
//			for(CityState cityState:acceptable_city_names){
//				strBldr.append("<p>" + cityState.getCity() + ", " + cityState.getState());
//			}
//		}
		return strBldr.toString();
	}
}

