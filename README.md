## Duscan

duplicate scanner with GUI


Program works in 3 modes:

0. Find duplicates (by default). The program creates a file list of choosen directory (and it's subdirs), then groups files with equal size, next, takes some bytes (checkpoints) from each file (like hashing) and compares them.

1. Find duplicates among images. Works same as default mode, but finds only jpg, png and bmp files, doesn't group by size, and takes some pixels as checkpoints instead of bytes. It allows to found not just duplicates but similar pictures, for example, same image with different resolutions. Slower, makes a lot of mistakes and (depending on your computer's RAM size) doesn't like large images.

2. Find images by filter. You set a lower border for image width and height and program just look for all images that are smaller. It also doesn't like images 6 MiB and more.

Written in: Java 8, Core + Swing.

License: BSD.
