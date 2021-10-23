package com.collab.project.util;

import flexjson.transformer.AbstractTransformer;
import flexjson.transformer.Transformer;

public class ExcludeNullTransformer extends AbstractTransformer {

    public ExcludeNullTransformer() {
    }

    @Override
    public void transform(Object o) {

    }
    public Boolean isInline() {
        return true;
    }
}
