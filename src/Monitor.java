import processing.core.*;
import processing.data.*;
import processing.event.*;
import processing.opengl.*;

import controlP5.*;

import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class Monitor extends PApplet {

	/* JACOB TURPIN 2/11/14
	 * 
	 * FORCE MONITOR SYSTEM PROOF OF CONCEPT
	 * 
	 * INTENDED FOR THE INTERAL USE BY THE UNC SCHOOL OF MEDICINE
	 * AND UNC BIOMEDICAL ENGINEERING DEPARTMENTS ONLY
	 * 
	 * 
	 * 
	 * GOALS:
	 * 		- PROVIDE PROOF OF CONCEPT
	 * 		- BASIC LAYOUT FOR DATA VISUALIZATION
	 * 		- DEMONSTRATE ABILITY TO COLLECT AND SAVE DATA POINTS
	 * 
	 * NEXT STEPS:
	 * 		1) GENERATE SERAIL INPUT (STRING W/ COMMA-DELIMTED VALUES
	 * 		2) GENERATE ASSETS FOR DISPLAY (A.K.A. MAKE IT LOOK PRETTIER)
	 * 		3) CONVERT FROM PROCESSING AND CONTROLP5 LIBRARIES TO SWING
	 * 		4) CONSOLIDATE AND TIDY UP CODE
	 * 
	 */

	ControlP5 cp5;

	String patientIDstr, dateStr, fileName, str;

	Textfield PatientID;
	Textfield Date;
	Button Collect;
	Button Save;

	Table table;
	float t = 0;

	int c1, c2;

	// Initialization conditions
	public void setup() {

		// Create table that will store values
		table = new Table();
		table.addColumn("Time");
		table.addColumn("Forehead");
		table.addColumn("Left Shoulder");
		table.addColumn("Right Shoulder");
		table.addColumn("Left Hip");
		table.addColumn("Right Hip");
		table.addColumn("Left Knee");
		table.addColumn("Right Knee");

		// Defining applet dimensions and background color
		size(1280, 720);
		background(188, 188, 188);
		
		// Generate outline for device
		fill(255, 255, 255);	// Set fill to WHITE (Stroke should still be default BLACK)
		rect(40, 40, 960, 640); // Device outline
		rect(80, 80, 80, 240); // Knee Pad 1
		rect(80, 400, 80, 240); // Knee Pad 2
		rect(880, 240, 80, 240); // Forehead Pad
		quad(280, 280, 400, 160, 480, 240, 360, 360); // Hip Pad 1
		quad(280, 440, 360, 360, 480, 480, 400, 560); // Hip Pad 2
		// Shoulder Pads
		line(560, 120, 800, 120);
		line(800, 120, 800, 600);
		line(800, 600, 560, 600);
		line(560, 600, 560, 520);
		line(560, 520, 720, 520);
		line(720, 520, 720, 200);
		line(720, 200, 560, 200);
		line(560, 200, 560, 120);
		rect(1020, 40, 240, 640); // User Control Outline

		// Header Text
		fill(0, 0, 0);	// BLACK
		textSize(36);
		text("Force", 1090, 80);
		text("Monitor", 1070, 110);
		fill(255, 255, 255); // WHITE

		cp5 = new ControlP5(this);
		// Adding Text Fields
		PatientID = cp5.addTextfield("Patient ID").setPosition(1040, 323)
				.setSize(200, 40).setAutoClear(false);
		Date = cp5.addTextfield("Date").setPosition(1040, 400).setSize(200, 40)
				.setAutoClear(false);
		Collect = cp5.addButton("Collect", 1, 1040, 525, 200, 60);
		Save = cp5.addButton("Save", 1, 1040, 600, 200, 60);

		// Adding text for the user controls
		fill(0, 0, 0);
		textSize(20);
		text("Patient ID", 1050, 313);
		text("Date", 1050, 390);
		text("Data Collection", 1050, 515);

		c1 = color(255, 255, 0); // Yellow
		c2 = color(255, 0, 0); // Red
		// Loop creates the rectangle below the header with Yellow-Red gradient
		for (int i = 1042; i <= 1042 + 200; i++) {
			float inter = map(i, 1042, 1042 + 200, 0, 1);
			int c = lerpColor(c1, c2, inter);
			stroke(c);
			line(i, 150, i, 150 + 30);
		}
		noFill();
		stroke(0); // Black outlines
		rect(1042, 150, 200, 30); // Border around gradient created above

		// Adding text for gradient "legend"
		textSize(12);
		text("Minimum", 1030, 195);
		text("Force", 1040, 210);
		text("Maximum", 1200, 195);
		text("Force", 1213, 210);
	}

	// DRAW = CONTINUOUSLY EXECUTING LOOP
	public void draw() {
		updateSensors();
		t = t + 0.005f;
	}

	public void updateSensors() {
		// TODO Automate??
		
		// Left Knee
		fill(255, abs(cos(t) * 255 * 0.5f), 0);
		rect(85, 165, 70, 70);
		// Right Knee
		fill(255, abs(cos(t) * 255 * 0.5f), 0);
		rect(85, 485, 70, 70);
		// Forehead
		fill(255, abs(cos(t) * 255 * 0.5f), 0);
		rect(885, 325, 70, 70);
		// Left Shoulder
		fill(255, abs(cos(t) * 255 * 0.5f + 120), 0);
		rect(725, 125, 70, 70);
		// Right Shoulder
		fill(255, abs(cos(t) * 255 * 0.5f + 120), 0);
		rect(725, 525, 70, 70);
		// Left Hip - Upper
		fill(255, abs(cos(t) * 255), 0);
		quad(400, 190, 450, 240, 400, 290, 350, 240);
		// Right Hip - Upper
		fill(255, abs(cos(t) * 255), 0);
		quad(400, 430, 450, 480, 400, 530, 350, 480);

	}

	// Using this function to add new data to a table
	public void addData() {
		TableRow newRow = table.addRow();
		newRow.setFloat("Time", t);
		newRow.setFloat("Forehead", abs(cos(t) * 255 * 0.5f));
		newRow.setFloat("Left Shoulder", abs(cos(t) * 255 * 0.5f + 120));
		newRow.setFloat("Right Shoulder", abs(cos(t) * 255 * 0.5f + 120));
		newRow.setFloat("Left Hip", abs(cos(t) * 255));
		newRow.setFloat("Right Hip", abs(cos(t) * 255));
		newRow.setFloat("Left Knee", abs(cos(t) * 255 * 0.5f));
		newRow.setFloat("Right Knee", abs(cos(t) * 255 * 0.5f));
	}

	public void saveData() {
		patientIDstr = PatientID.getText();
		dateStr = Date.getText();

		patientIDstr = patientIDstr.replaceAll("[^a-zA-Z0-9]+", "");
		dateStr = dateStr.replaceAll("[^a-zA-Z0-9]+", "");

		fileName = patientIDstr + "_" + dateStr;
		saveTable(table, "C:/Users/Jacob/Desktop/" + fileName + ".csv"); 
		// TODO Alternate such that files save to Desktop w/o absolute filepath
	}

	public void controlEvent(ControlEvent theEvent) {
		if (theEvent.isController()) {
			if (theEvent.controller().name() == "Collect") { addData();	}
			if (theEvent.controller().name() == "Save") { saveData(); }
		}
	}

	static public void main(String[] passedArgs) {
		String[] appletArgs = new String[] { "Point_Force_Prototype" };
		if (passedArgs != null) { PApplet.main(concat(appletArgs, passedArgs)); 
		} else { PApplet.main(appletArgs); }
	}
	
}
