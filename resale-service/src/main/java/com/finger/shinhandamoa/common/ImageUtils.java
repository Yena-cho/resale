package com.finger.shinhandamoa.common;

import com.mortennobel.imagescaling.ResampleFilters;
import com.mortennobel.imagescaling.ResampleOp;
import com.sun.media.jai.codec.SeekableStream;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;

import javax.imageio.*;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author wisehouse@finger.co.kr
 */
public class ImageUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageUtils.class);
    private static final String FORMAT_NAME_JPEG = "jpeg";
    private static String JPEGMetaFormat = "javax_imageio_jpeg_image_1.0";
    private static IIOMetadata DPI;

    public static boolean resizeJpeg(File from, File to, int minLength) {
        return resizeJpeg(from, to, minLength, minLength);
    }

    public static boolean resizeJpeg(File from, File to, int minWidth, int minHeight) {
        return resizeJpeg(from, to, minWidth, minHeight, true);
    }

    /**
     * 이미지 확대/축소
     *
     * @param from                원본 파일
     * @param to                  결과 파일
     * @param width               목적 폭
     * @param height              목적 높이
     * @param scaleProportionally 비율 고정 여부
     * @return
     */
    public static boolean resizeJpeg(File from, File to, int width, int height, boolean scaleProportionally) {
        if (to.exists()) {
            return true;
        }

        if (width == 0 && height == 0) {
            return false;
        }

        try {
            BufferedImage is = ImageIO.read(from);

            double widthRate;
            double heightRate;
            if (width == 0) {
                widthRate = ((double) height) / is.getHeight();
                heightRate = widthRate;
            } else if (height == 0) {
                widthRate = ((double) width) / is.getWidth();
                heightRate = widthRate;
            } else if (scaleProportionally) {
                widthRate = Math.max(((double) width) / is.getWidth(), ((double) height) / is.getHeight());
                heightRate = widthRate;
            } else {
                widthRate = ((double) width) / is.getWidth();
                heightRate = ((double) height) / is.getHeight();
            }
            int newWidth = (int) (is.getWidth() * widthRate);
            int newHeight = (int) (is.getHeight() * heightRate);
            double minRate = Math.min(widthRate, heightRate);

            LOGGER.debug("image width:{}, image height:{}, target width: {}, target height: {}, widthRate: {}, heightRate: {}, scaleProportionally: {}", is.getWidth(), is.getHeight(), newWidth, newHeight, widthRate, heightRate, scaleProportionally);

            BufferedImage scaledImage = resizeBufferedImage(is, newWidth, newHeight);

            Iterator<ImageWriter> writers = ImageIO.getImageWritersBySuffix("jpeg");
            ImageWriter writer = writers.next();
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);

            float compressionQuality;
            if(minRate <= 0.4) {
                compressionQuality = 0.93F;
            } else if(minRate <= 0.5) {
                compressionQuality = 0.86F;
            } else {
                compressionQuality = 0.77F;
            }
            param.setCompressionQuality(compressionQuality);
            param.setProgressiveMode(ImageWriteParam.MODE_DEFAULT);
            if(param instanceof JPEGImageWriteParam) {
                ((JPEGImageWriteParam) param).setOptimizeHuffmanTables(true);
            }

            ImageOutputStream ios = ImageIO.createImageOutputStream(to);
            
            writer.setOutput(ios);
            writer.write(null, new IIOImage(scaledImage, null, null), param);
            writer.dispose();
            
            ios.close();
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ResampleFilters.getLanczos3Filter()
     * 
     * @param bufferedImage
     * @param width
     * @param height
     * @return
     */
    private static BufferedImage resizeBufferedImage(BufferedImage bufferedImage, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, bufferedImage.getType());
        ResampleOp resampleOp = new ResampleOp(width, height);
//        resampleOp.setFilter(ResampleFilters.getLanczos3Filter());
        resampleOp.setFilter(ResampleFilters.getBiCubicFilter());
        resampleOp.filter(bufferedImage, scaledImage);
        
        return scaledImage;
    }

    public static boolean rotate(File from, File to, float degree) {
        if (degree % 360 == 0) {
            try {
                FileUtils.copyFile(from, to);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        try (FileInputStream fis = new FileInputStream(from); FileOutputStream fos = new FileOutputStream(to)) {
            SeekableStream s = SeekableStream.wrapInputStream(new BufferedInputStream(fis), true);
            RenderedOp image = JAI.create("stream", s);

            ParameterBlock pb = new ParameterBlock();
            pb.addSource(image);

            pb.add(new Float(0)); // x-origin
            pb.add(new Float(0)); // y-origin
            pb.add(new Float(Math.PI * 2 / 360 * degree)); // rotation amount

            RenderedOp image2 = JAI.create("rotate", pb);

            return ImageIO.write(image2, "jpg", new BufferedOutputStream(fos));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static byte[] getEXIF(IIOMetadata meta) {
        //http://java.sun.com/javase/6/docs/api/javax/imageio/metadata/doc-files/jpeg_metadata.html

        //javax_imageio_jpeg_image_1.0
        //-->markerSequence
        //---->unknown (attribute: "MarkerTag" val: 225 (for exif))

        IIOMetadataNode root = (IIOMetadataNode) meta.getAsTree(JPEGMetaFormat);

        IIOMetadataNode markerSeq = (IIOMetadataNode)
                root.getElementsByTagName("markerSequence").item(0);

        NodeList unkowns = markerSeq.getElementsByTagName("unknown");
        for (int i = 0; i < unkowns.getLength(); i++) {
            IIOMetadataNode marker = (IIOMetadataNode) unkowns.item(i);
            if ("225".equals(marker.getAttribute("MarkerTag"))) {
                return (byte[]) marker.getUserObject();
            }
        }
        return null;
    }

    public static Map<String, Object> getMetadata(File file) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            ImageInputStream imageInputStream = ImageIO.createImageInputStream(file);
            Iterator<ImageReader> readers = ImageIO.getImageReaders(imageInputStream);
            while (readers.hasNext()) {
                ImageReader each = readers.next();

                each.setInput(imageInputStream, true);
                int height = each.getHeight(0);
                int width = each.getWidth(0);

                resultMap.put("width", width);
                resultMap.put("height", height);
            }
        } catch (IOException e) {
            LOGGER.warn(e.getMessage(), e);
        }

        return resultMap;
    }

    private static void setDPI(IIOMetadata metadata) throws IIOInvalidTreeException {
        int DPI = 36;
        float INCH_2_CM = 2.54F;

        // for PMG, it's dots per millimeter
        double dotsPerMilli = 1.0 * DPI / 10 / INCH_2_CM;

        IIOMetadataNode horiz = new IIOMetadataNode("HorizontalPixelSize");
        horiz.setAttribute("value", Double.toString(dotsPerMilli));

        IIOMetadataNode vert = new IIOMetadataNode("VerticalPixelSize");
        vert.setAttribute("value", Double.toString(dotsPerMilli));

        IIOMetadataNode dim = new IIOMetadataNode("Dimension");
        dim.appendChild(horiz);
        dim.appendChild(vert);

        IIOMetadataNode root = new IIOMetadataNode("javax_imageio_1.0");
        root.appendChild(dim);

        metadata.mergeTree("javax_imageio_1.0", root);
    }}
