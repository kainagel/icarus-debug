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

	public static void main(String[] args) {

		Config config ;

		if ( args==null || args.length==0 ){
			config = ConfigUtils.loadConfig( "scenarios/icarus-debug/config.xml" );
		} else {
			config = ConfigUtils.loadConfig( args ) ;
		}

		// possibly modify config here
		config.controler().setOverwriteFileSetting( OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists );
		config.controler().setOutputDirectory( "output" );
		config.controler().setLastIteration( 0 );

		config.plans().setInputFile( "plans-reduced.xml" );

//		config.transit().setUseTransit( false );
//		config.plansCalcRoute().addModeRoutingParams( new ModeRoutingParams( TransportMode.pt ).setTeleportedModeFreespeedFactor( 2. ) );

		SwissRailRaptorConfigGroup raptorConfig = ConfigUtils.addOrGetModule( config, SwissRailRaptorConfigGroup.class );
		raptorConfig.setUseIntermodalAccessEgress( true );

		// so that command line argument are applied _after_ all the above material:
		if ( args!=null && args.length>=1 ){
			String[] typedArgs = Arrays.copyOfRange( args, 1, args.length );
			ConfigUtils.applyCommandline( config, typedArgs ) ;
		}


		// ---
		
		Scenario scenario = ScenarioUtils.loadScenario(config) ;
		
		// possibly modify scenario here
		
		// ---
		
		Controler controler = new Controler( scenario ) ;
		
		// possibly modify controler here

		controler.addOverridingModule( new SwissRailRaptorModule() );

//		controler.addOverridingModule( new OTFVisLiveModule() ) ;
		
		// ---
		
		controler.run();
	}
	
}
