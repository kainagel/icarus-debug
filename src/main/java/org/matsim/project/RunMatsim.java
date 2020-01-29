/* *********************************************************************** *
 * project: org.matsim.*												   *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2008 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */
package org.matsim.project;

import ch.sbb.matsim.config.SwissRailRaptorConfigGroup;
import ch.sbb.matsim.routing.pt.raptor.SwissRailRaptorModule;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.PlansCalcRouteConfigGroup;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.scenario.ScenarioUtils;

import java.util.Arrays;

import static org.matsim.core.config.groups.PlansCalcRouteConfigGroup.*;

/**
 * @author nagel
 *
 */

public class RunMatsim{
	private static final Logger log = Logger.getLogger(RunMatsim.class);

	public static void main(String[] args) {

		Config config ;

		if ( args==null || args.length==0 ){
			config = ConfigUtils.loadConfig( "scenarios/simplified/config.xml" );
		} else {
			config = ConfigUtils.loadConfig( args ) ;
		}

		// CONFIG MODIFICATION

		config.controler().setOverwriteFileSetting( OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists );
		config.controler().setOutputDirectory( "output" );
		config.controler().setLastIteration( 0 );

//		config.transit().setUseTransit( false );
//		config.plansCalcRoute().addModeRoutingParams( new ModeRoutingParams( TransportMode.pt ).setTeleportedModeFreespeedFactor( 2. ) );

		// SWISS RAIL RAPTOR SETUP

		// not sure why, but this module never loads correctly from config
		// will always need to setup swissRailRaptorHere

//		SwissRailRaptorConfigGroup raptorConfig = ConfigUtils.addOrGetModule( config, SwissRailRaptorConfigGroup.class );
//		raptorConfig.setUseIntermodalAccessEgress( true );
//		SwissRailRaptorConfigGroup.IntermodalAccessEgressParameterSet walkEgress = null;
//		String walkType = "netwalk";
//
//		for(SwissRailRaptorConfigGroup.IntermodalAccessEgressParameterSet egress : raptorConfig.getIntermodalAccessEgressParameterSets())
//			if(walkType.equals(egress.getMode()))
//				walkEgress = egress;
//		if (walkEgress == null) {
//			log.warn("Did not find raptor egress parameters in config; manually adding them.");
//			walkEgress = new SwissRailRaptorConfigGroup.IntermodalAccessEgressParameterSet();
//			walkEgress.setMode(walkType);
//			walkEgress.setInitialSearchRadius(1000.0);
//			walkEgress.setMaxRadius(2000.0);
//			walkEgress.setSearchExtensionRadius(500.0);
//			raptorConfig.addIntermodalAccessEgress(walkEgress);
//		}

		// PARSE COMMAND LINE ARGS

		if ( args!=null && args.length>=1 ){
			String[] typedArgs = Arrays.copyOfRange( args, 1, args.length );
			ConfigUtils.applyCommandline( config, typedArgs );
		}

		// MODIFY SCENARIO

		Scenario scenario = ScenarioUtils.loadScenario(config);
		
		// MODIFY CONTROLER

		Controler controler = new Controler( scenario );

//		controler.addOverridingModule( new SwissRailRaptorModule() );
//		controler.addOverridingModule( new OTFVisLiveModule() ) ;
		
		// RUN SIMULATION
		
		controler.run();
	}
	
}
