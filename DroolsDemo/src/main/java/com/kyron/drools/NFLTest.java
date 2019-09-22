package com.kyron.drools;

import org.kie.api.runtime.KieSession;

import com.kyron.drools.config.DroolsBeanFactory;
import com.kyron.drools.model.Matchup;
import com.kyron.drools.model.Team;

public class NFLTest{

	public static void main(String[] args) {
		
        // The application can insert facts into the session
        KieSession ksession = new DroolsBeanFactory().getKieSession();
        Team home = new Team("WAS", true, "NFC EAST");
        Team visitor = new Team("DAL", false, "NFC EAST");
        Matchup game1 = new Matchup(home, visitor);
        ksession.insert(home);
        ksession.insert(visitor);
        ksession.insert(game1);

        // and fire the rules
        ksession.fireAllRules();
        System.out.println("program end");
	}

}
