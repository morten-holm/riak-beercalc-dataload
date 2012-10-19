package dk.muhko;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class App {
    public static void main(String[] args) throws Exception {
/*
        ObjectMapper mapper = new ObjectMapper();
        List<Beer> beers = new BeerService().fetchAllBeers();
        System.out.println(mapper.writeValueAsString(beers));
*/

        boolean delete = false;
        if (delete) {
            new RiakBeerRepository().emptyBeerBucket();
        }

        boolean load = true;
        if (load) {
            File beerDir = new File("output");
            final File[] files = beerDir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    //return pathname.getName().startsWith("99");
                    return true;
                }
            });

            BlockingQueue<File> queue = new SynchronousQueue<File>();
            List<Beer> beers = Collections.synchronizedList(new ArrayList<Beer>());

            for (int i = 0; i < 4; i++) {
                final Thread thread = new Thread(new BeerCreator(queue, beers));
                thread.setDaemon(true);
                thread.start();
                System.out.print(".");
            }

            for (File file : files) {
                queue.put(file);
            }

            while (!queue.isEmpty()) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    // ignore
                }
            }
            new RiakBeerRepository().load(beers);
        }
    }

    private static class BeerCreator implements Runnable {
        private final BlockingQueue<File> queue;
        private final List<Beer> beers;

        public BeerCreator(BlockingQueue<File> queue, List<Beer> beers) {
            this.queue = queue;
            this.beers = beers;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    beers.add(new BeerService().createBeer(queue.take()));
                }
            } catch (InterruptedException e) {
                // ignore
            }
        }
    }
}
