import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;


public class ImageReader {

    BufferedImage img;
    public ArrayList<BufferedImage> ov;

    public void showIms(String[] args) {
        int width = Integer.parseInt(args[1]);
        int height = Integer.parseInt(args[2]);
        int fps = Integer.parseInt(args[3]);

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        ov = new ArrayList<BufferedImage>();

        try {
            File file = new File(args[0]);
            InputStream is = new FileInputStream(file);

            long len = file.length();
            byte[] bytes = new byte[(int) len];

            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }


            int ind = 0;

            for (int i = 0; i < (bytes.length)/(width * height * 3); i++) {
                img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

                for (int y = 0; y < height; y++) {

                    for (int x = 0; x < width; x++) {

                        byte a = 0;
                        byte r = bytes[ind];
                        byte g = bytes[ind + height * width];
                        byte b = bytes[ind + height * width * 2];

                        int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
                        //int pix = ((a << 24) + (r << 16) + (g << 8) + b);
                        img.setRGB(x, y, pix);
                        ind++;
                    }
                }

                ind += height*width*2;	
                ov.add(img);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {

        }

        // Use a label to display the image
        JFrame frame = new JFrame();
        JLabel label = new JLabel(new ImageIcon(ov.get(0)));
        frame.getContentPane().add(label, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        // Set frame rate
        long startTime = System.currentTimeMillis();
        long endTime;

        while(true) {
            for (int i = 1; i < ov.size(); i++) {
                label.setIcon(new ImageIcon(ov.get(i)));
                try {
                    Thread.sleep(1000 / fps); // 10000/30 should be default
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            endTime = System.currentTimeMillis();
            System.out.println("Completed in " + (endTime - startTime)/1000  + " seconds"  ) ;
        }


    }

    public static void main(String[] args) {
        ImageReader ren = new ImageReader();
        ren.showIms(args);
    }

}