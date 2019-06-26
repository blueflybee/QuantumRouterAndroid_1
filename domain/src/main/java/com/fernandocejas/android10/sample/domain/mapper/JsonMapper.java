package com.fernandocejas.android10.sample.domain.mapper;

import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * @author shaojun
 * @name JsonMapper
 * @package com.fernandocejas.android10.sample.data.entity.mapper
 * @date 15-9-11
 * <p>
 * Json mapper transforms json to object/object to json.
 */
public class JsonMapper {

    public JsonMapper() {
    }

    public String toJson(Object encryptInfo) {
        return new Gson().toJson(encryptInfo);
    }

    public Object fromJson(String json, Type type) {
        return new Gson().fromJson(json, type);
    }


}
