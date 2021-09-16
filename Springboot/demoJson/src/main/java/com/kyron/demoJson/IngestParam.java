package com.kyron.demoJson;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//import com.fasterxml.jackson.annotation.JsonGetter;
//
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;

/**
 * This annotation documents an ingest component's parameter.
 */
//@Retention(RetentionPolicy.RUNTIME)
//public @interface IngestParam {

@Getter @Setter @NoArgsConstructor
public class IngestParam {

	public IngestJobParam param;

	public boolean required;

}
