package kyron.com.helloKube;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin
@RequestMapping(path="/") 
public class StudentController {
	@Autowired
	private ConfigDTO config;
	
	// for volume mounted configmap
	@Value("${user.myvar1:default-value}")
	private String userMyVar1;
	
	// smoke test to see if server is up
	@GetMapping("/hello")
	public String hello(){
		return "Hello world";
	}
	
	// test @PathVariable
	@GetMapping("/get/{id}")
	public String getOne( @PathVariable String id) {
		return ("@PathVariable id = " + id);
	}
	
	@GetMapping("/greeting")
	public String getEnvVar() {
		String prefix = System.getenv().getOrDefault("GREETING_PREFIX", "unknown");
		return prefix;
	}
	
	@GetMapping("/configdto")
	public String getConfigDTO() {
		return (config.getMyvar1());
	}
	
	@GetMapping("/configvalue")
	public String getConfigValue() {
		return (userMyVar1);
	}
	

}
