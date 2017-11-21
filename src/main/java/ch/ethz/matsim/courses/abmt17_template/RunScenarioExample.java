package ch.ethz.matsim.courses.abmt17_template;

import ch.ethz.matsim.av.electric.NewRechargingModule;
import ch.ethz.matsim.av.electric.RechargingConfig;
import ch.ethz.matsim.av.electric.RechargingModule;
import ch.ethz.matsim.av.electric.calculators.StaticChargeCalculatorConfig;
import ch.ethz.matsim.av.electric.calculators.StaticChargeCalculatorModule;
import ch.ethz.matsim.av.framework.AVConfigGroup;
import ch.ethz.matsim.av.framework.AVModule;
import ch.ethz.matsim.av.framework.AVQSimProvider;
import org.matsim.api.core.v01.Scenario;
import org.matsim.contrib.dvrp.run.DvrpConfigGroup;
import org.matsim.contrib.dvrp.trafficmonitoring.DvrpTravelTimeModule;
import org.matsim.contrib.dynagent.run.DynQSimModule;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.scenario.ScenarioUtils;

import abmt17.pt.ABMTPTModule;
import abmt17.scoring.ABMTScoringModule;
import ch.ethz.matsim.baseline_scenario.analysis.simulation.ModeShareListenerModule;

/**
 * This is the template for the 2017 ABMT course at ETHZ
 * 
 * With this example you can run the scenario. The only argument
 * to the script is the path to the config file (abmt_config.xml).
 * 
 * In Eclipse, for instance, you can right click on the "main"
 * method and choose "Run As ..." -> "Java Application". At
 * first you will get an error, because no command line argument
 * has been provided. In the run menu "Green Arrow" under the menu
 * bar you can now click on "Run configurations ..." where you
 * can choose "RunScenarioExample". Click on "Arguments" and 
 * set the command line arguments to "abmt_config.xml".
 * 
 * Furthermore, add the following to VM aruments:
 * -Xmx10G 
 * It will tell Java to use up to 10GB of RAM for the simulation.
 */
public class RunScenarioExample {
	static public void main(String[] args) {
		Config config = ConfigUtils.loadConfig(args[0],new AVConfigGroup(), new DvrpConfigGroup(),new RechargingConfig(), new StaticChargeCalculatorConfig()); // Load the config file (command line argument)

		Scenario scenario = ScenarioUtils.loadScenario(config); // Load scenario
		Controler controler = new Controler(scenario); // Set up simulation controller

		// Some additional modules to create a more realistic simulation

		controler.addOverridingModule(new ABMTPTModule()); // More realistic "teleportation" of public transport trips
		controler.addOverridingModule(new ModeShareListenerModule()); // Writes correct mode shares in every iteration
		controler.addOverridingModule(new DvrpTravelTimeModule());
		controler.addOverridingModule(new DynQSimModule<>(AVQSimProvider.class));
		controler.addOverridingModule(new AVModule());
		controler.addOverridingModule(new NewRechargingModule());
		controler.addOverridingModule(new ABMTScoringModule()); // Required if scoring of activities is used
		controler.addOverridingModule(new StaticChargeCalculatorModule());


		controler.run();
	}
}
