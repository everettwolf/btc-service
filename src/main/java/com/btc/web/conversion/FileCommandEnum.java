package com.btc.web.conversion;

/**
 * Created by Chris on 9/19/15.
 */
public enum FileCommandEnum {
     MP4_TO_OGV_320x240("ffmpeg2theora", "--videobitrate 200 --max_size 640x360 --output output.ogv input.mp4"),
     MP4_TO_H256_320x240("HandBrakeCLI","--preset 'iPhone & iPod Touch' --vb 200 --width 320 --two-pass --turbo --optimize --input input.mp4 --output output.mp4"),
     MP4_TO_THUMBNAILS("ffmpeg", "-i input.mp4 -r 1 -s 640x360 -f image2 -vframes 10 output-%03d.png");

     //for i in *C.mp4; do name=`echo $i | cut -d'.' -f1`; echo $name; ffmpeg -i $i -s 640x360 $name.webm; done

     private String program;
     private String command;

     FileCommandEnum(String program, String command) {
          this.program = program;
          this.command = command;
     }
}
