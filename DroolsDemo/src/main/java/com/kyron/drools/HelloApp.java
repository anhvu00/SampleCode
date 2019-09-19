package com.kyron.drools;

import org.kie.api.runtime.KieSession;

import com.kyron.drools.config.DroolsBeanFactory;
import com.kyron.drools.model.Message;

public class HelloApp {

	public static void main(String[] args) {
		
        // The application can insert facts into the session
        KieSession ksession = new DroolsBeanFactory().getKieSession();
        final Message message = new Message();
        message.setMessage( "Hello World" );
        message.setStatus( Message.HELLO );
        ksession.insert( message );

        // and fire the rules
        ksession.fireAllRules();
        System.out.println("stop here for debug");
	}

}
