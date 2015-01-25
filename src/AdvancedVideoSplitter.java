import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.TimeUnit;

import com.xuggle.mediatool.IMediaListener;
import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;

public class AdvancedVideoSplitter {
	
	private File videoFile=null;
	private int timeIntervalInMin=15;

//	private static final TimeUnit TIME_UNIT = TimeUnit.MICROSECONDS;	
	
	public AdvancedVideoSplitter(File video, int timeInterval) {
		if(video==null || !video.exists() || video.isDirectory()) {
			throw new RuntimeException("The video file is not valid:"+video);
		}
		videoFile=video;
		if(timeInterval>=5) {
			timeIntervalInMin=timeInterval;
		}
	}
	
	public void splitFiles() throws Exception {
		long inputTimeIntervalInMillies=TimeUnit.MICROSECONDS.convert(timeIntervalInMin, TimeUnit.MINUTES);
		//create a media reader
	  	IMediaReader mediaReader = ToolFactory.makeReader(videoFile.getAbsolutePath());
	  	 // have the reader create a buffered image that others can reuse
	  	mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
	  	Cutter cutter=new Cutter();
	  	
		//add a viewer to the reader, to see the decoded media
		mediaReader.addListener((IMediaListener) cutter);
		//read and decode packets from the source file and
		//dispatch decoded audio and video to the writer
		int fileCounter=1;
		String url=videoFile.getAbsolutePath().substring(0, videoFile.getAbsolutePath().lastIndexOf("."))+"_"+fileCounter+".mp4";
		IMediaWriter writer=ToolFactory.makeWriter(url,mediaReader);
		cutter.addListener(writer);
		while (mediaReader.readPacket()==null) {
			System.out.println("cutter.getTimeCounter()="+cutter.getTimeCounter());			
			if(cutter.getTimeCounter()>inputTimeIntervalInMillies) {
				System.out.println("inputTimeIntervalInMillies="+inputTimeIntervalInMillies);
				cutter.removeListener(writer);
				writer.close();// flusing and closing earlier writers..
				fileCounter++;
				url=videoFile.getAbsolutePath().substring(0, videoFile.getAbsolutePath().lastIndexOf("."))+"_"+fileCounter+".mp4";
				writer=ToolFactory.makeWriter(url,mediaReader);
				writer.addListener(ToolFactory.makeDebugListener());
				inputTimeIntervalInMillies=inputTimeIntervalInMillies+TimeUnit.MICROSECONDS.convert(timeIntervalInMin, TimeUnit.MINUTES); // next time slot..
				cutter.addListener(writer);
			}
		}
		writer.close();// flusing and closing earlier writers..
		mediaReader.close();
	}

	public class Cutter extends MediaToolAdapter { 

		private long timeCounterInMillies=0;
	
		@Override
		public void onVideoPicture(IVideoPictureEvent arg0) {
			timeCounterInMillies=arg0.getTimeStamp();
			super.onVideoPicture(arg0);
		}
		
		public void addListener(IMediaWriter writer) {
			// TODO Auto-generated method stub
			
		}

		public long getTimeCounter() {
			return timeCounterInMillies;
		}
	}
	/**
	 * The tester..
	 * @param args
	 * @throws Exception
	 */
	

}