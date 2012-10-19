package dk.muhko;

import com.basho.riak.client.IRiakClient;
import com.basho.riak.client.RiakException;
import com.basho.riak.client.RiakFactory;
import com.basho.riak.client.bucket.Bucket;
import com.google.common.collect.Lists;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

public class RiakBeerRepository {

    public void load(List<Beer> beers) {
        IRiakClient myDefaultPbClient = null;
        try {
            myDefaultPbClient = RiakFactory.pbcClient("127.0.0.1", 8081);
            Bucket myBucket = myDefaultPbClient.fetchBucket("beers").execute();

            int i = 1;
            for (Beer beer : beers) {
                myBucket.store(String.valueOf(i++), toJSON(beer)).execute();
                System.out.print(".");
            }
        } catch (RiakException e) {
            throw new RuntimeException(e);
        } finally {
            if (myDefaultPbClient != null) {
                myDefaultPbClient.shutdown();
            }
        }
    }

    public void emptyBeerBucket() {
        IRiakClient myDefaultPbClient = null;
        try {
            myDefaultPbClient = RiakFactory.pbcClient("127.0.0.1", 8081);
            Bucket myBucket = myDefaultPbClient.fetchBucket("beers").execute();
            List<String> keys = Lists.newArrayList(myBucket.keys());
            for (String key : keys) {
                myBucket.delete(key).execute();
            }
        } catch (RiakException e) {
            throw new RuntimeException(e);
        } finally {
            if (myDefaultPbClient != null) {
                myDefaultPbClient.shutdown();
            }
        }
    }

    private String toJSON(Beer beer) {
        try {
            StringWriter writer = new StringWriter();
            new ObjectMapper().writeValue(writer, beer);
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
