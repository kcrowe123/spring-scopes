package com.jyore.spring.scope.route.example.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jyore.spring.scope.route.example.beans.InterceptInspector;
import com.jyore.spring.scope.route.example.processor.BodyListSetter;
import com.jyore.spring.scope.route.example.processor.ValueChecker;
import com.jyore.spring.scope.route.example.processor.ValueSetter;


@Component
public class SplitRoute extends RouteBuilder {

	@Autowired
	private ValueSetter valueSetter;
	
	@Autowired
	private ValueChecker valueChecker;
	
	@Autowired
	private BodyListSetter bodyList;
	
	@Autowired
	private InterceptInspector interceptInspector;
	
	@Override
	public void configure() throws Exception {
		
		//Contextual printout of route information
		intercept()
			.bean(interceptInspector,"process")
		;
		
		from("timer://split?fixedRate=true&period=5000")
			.log("TIMER 1")
			.delay(1000)
			.log("TIMER 2")
			.bean(valueSetter,"process")
			.bean(bodyList,"process")
			.split(body().tokenize(",")).parallelProcessing()
				.bean(valueChecker,"process")
				.log("TIMER 3")
			
		;
	}

}