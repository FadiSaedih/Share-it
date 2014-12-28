
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.imageio.ImageIO;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;

import com.xuggle.mediatool.*;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.Global;


public class Test {
	public static final double SECONDS_BETWEEN_FRAMES = 10;
	private static final String inputFilename = "e:/low_light.mp4";
	private static final String outputFilePrefix = "C:/Frames/";
	// The video stream index, used to ensure we display frames from one and
	// only one video stream from the media container.
	private static int mVideoStreamIndex = -1;
	// Time of last frame write
	private static long mLastPtsWrite = Global.NO_PTS;
	public static final long MICRO_SECONDS_BETWEEN_FRAMES =
			(long) (Global.DEFAULT_PTS_PER_SECOND * 10);
	public static void main(String []args) throws IOException, JCodecException{

		/*RandomAccessFile f = new RandomAccessFile("part1.mp4", "r");
		byte[] b = new byte[(int)f.length()];
		f.read(b);
		f.close();
		for (int i=0;i<b.length;i++)
			System.out.print(b[i]+" ");*/
		/*IMediaReader mediaReader = ToolFactory.makeReader("sam12.mp4");

		// stipulate that we want BufferedImages created in BGR 24bit color space
		mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);

		mediaReader.addListener(new ImageSnapListener());
		System.gc();
		// read out the contents of the media file and
		// dispatch events to the attached listener

		while (mediaReader.readPacket() == null){System.gc();} ;*/
		/*AdvancedVideoSplitter v=new AdvancedVideoSplitter(new File("sam10.mp4"),15);
		try {
			v.splitFiles();
		} catch (Exception e) {

			e.printStackTrace();
		}*/

		Long time = System.currentTimeMillis();
		double framenum=0;
		for (int i = 1; i < 6; i++) { 

			BufferedImage frame = FrameGrab.getFrame(new File("sam9.mp4"),(double) framenum);
			framenum=framenum+1;
			ImageIO.write(frame, "bmp", new File("C:/Frames/"+i+".bmp"));

		}

		System.out.println("Time Used:" + (System.currentTimeMillis() - time)+" Milliseconds");

	}
	private static class ImageSnapListener extends MediaListenerAdapter {

		public void onVideoPicture(IVideoPictureEvent event) {
			try{
				if (event.getStreamIndex() != mVideoStreamIndex) {

					// if the selected video stream id is not yet set, go ahead an
					// select this lucky video stream
					if (mVideoStreamIndex == -1) {
						mVideoStreamIndex = event.getStreamIndex();
					} // no need to show frames from this video stream
					else {
						return;
					}
				}

				// if uninitialized, back date mLastPtsWrite to get the very first frame
				if (mLastPtsWrite == Global.NO_PTS) {
					mLastPtsWrite = event.getTimeStamp() - MICRO_SECONDS_BETWEEN_FRAMES;
				}

				// if it's time to write the next frame
				if (event.getTimeStamp() - mLastPtsWrite
						>= MICRO_SECONDS_BETWEEN_FRAMES) {

					String outputFilename = dumpImageToFile(event.getImage());

					// indicate file written
					double seconds = ((double) event.getTimeStamp())
							/ Global.DEFAULT_PTS_PER_SECOND;
					System.out.printf(
							"at elapsed time of %6.3f seconds wrote: %s\n",
							seconds, outputFilename);

					// update last write time
					mLastPtsWrite += MICRO_SECONDS_BETWEEN_FRAMES;

				}
			}catch(Exception e){}

		}

		private String dumpImageToFile(BufferedImage image) {
			try {
				String outputFilename = outputFilePrefix
						+ System.currentTimeMillis() + ".png";
				ImageIO.write(image, "png", new File(outputFilename));
				return outputFilename;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}

	}
}
