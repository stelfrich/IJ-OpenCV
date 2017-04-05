package ijopencv.examples;

import static org.bytedeco.javacpp.opencv_imgproc.ADAPTIVE_THRESH_GAUSSIAN_C;
import static org.bytedeco.javacpp.opencv_imgproc.ADAPTIVE_THRESH_MEAN_C;
import static org.bytedeco.javacpp.opencv_imgproc.THRESH_BINARY;
import static org.bytedeco.javacpp.opencv_imgproc.THRESH_BINARY_INV;
import static org.bytedeco.javacpp.opencv_imgproc.adaptiveThreshold;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import net.imagej.ImageJ;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.command.CommandModule;
import org.scijava.module.Module;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import ij.ImagePlus;
import ij.process.ByteProcessor;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jonathan
 */
@Plugin( type = Command.class, menuPath = "Plugins>OpenCV>Adaptive Threshold" )
public class Adaptive_ThresholdJ_2 implements Command
{
	@Parameter(required = false)
	String method = "Mean";

	@Parameter(required = false)
	String thresholdmethod = "Binary";

	@Parameter(required = false)
	int maxValue = 255;

	@Parameter(required = false)
	int blockSize = 3;

	@Parameter
	Mat m;

	@Parameter( type = ItemIO.OUTPUT )
	private Mat out = new Mat();

	@Override
	public void run()
	{
		int adaptiveMethod;
		if ( method == "Mean" ) {
			adaptiveMethod = ADAPTIVE_THRESH_MEAN_C;
		} else {
			adaptiveMethod = ADAPTIVE_THRESH_GAUSSIAN_C;
		}

		int thresType;
		if ( thresholdmethod == "Binary" ){
			thresType = THRESH_BINARY;
		} else {
			thresType = THRESH_BINARY_INV;
		}

		adaptiveThreshold( m, out, maxValue, adaptiveMethod, thresType, blockSize, 2 );
	}

	public static void main( final String... args ) throws Exception
	{
		// create the ImageJ application context with all available services
		final ImageJ ij = new ImageJ();

		// populate the map of input parameters
		final Map< String, Object > inputMap = new HashMap<>();
		ByteProcessor ip = new ByteProcessor( 100, 100 );
		ip.noise( 20 );
		ImagePlus imp = new ImagePlus( "", ip );
		imp.show();
		inputMap.put( "m", imp );

		// execute asynchronously using the command service
		Future<CommandModule> future = ij.command().run( Adaptive_ThresholdJ_2.class, true, inputMap );
		// wait for the execution thread to complete
		final Module module = ij.module().waitFor( future );
		// return the desired output parameter value
		Mat out = ( Mat ) module.getOutput( "out" );

		// Manually convert from Mat to ImagePlus
		ImagePlus outImp = ij.convert().convert( out, ImagePlus.class );
		outImp.show();
	}
}
