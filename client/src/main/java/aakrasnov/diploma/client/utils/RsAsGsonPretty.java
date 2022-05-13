package aakrasnov.diploma.client.utils;

import aakrasnov.diploma.common.RsBaseDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RsAsGsonPretty {

    private final RsBaseDto res;

    private final Gson gson;

    public RsAsGsonPretty(final RsBaseDto res) {
        this(res, new Gson());
    }

    public RsAsGsonPretty(final RsBaseDto res, final Gson gson) {
        this.res = res;
        this.gson = gson;
    }

    public String convert(boolean isPretty) {
        String resText;
        if (isPretty) {
            resText = new GsonBuilder().setPrettyPrinting()
                .create().toJson(res);
        } else {
            resText = gson.toJson(res);
        }
        return resText;
    }
}
