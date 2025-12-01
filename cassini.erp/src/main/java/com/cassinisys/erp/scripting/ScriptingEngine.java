package com.cassinisys.erp.scripting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.scripting.ScriptEvaluator;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by reddy on 9/14/15.
 */
@Component
public class ScriptingEngine {
    @Autowired
    private ScriptEvaluator scriptEvaluator;

    @Autowired
    private RepositoriesBean repositoriesBean;

    @PersistenceContext
    private EntityManager entityManager;

    private Map<String, Object> variables = new HashMap<>();


    public Object executeScript(ScriptSource source) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.putAll(variables);
        parameters.put("_repos", repositoriesBean);
        parameters.put("_entityManager", entityManager);
        return scriptEvaluator.evaluate(source, parameters);
    }

    public void bindVariable(String name, Object value) {
        variables.put(name, value);
    }

    public Object executeScript(InputStream is) {
        return executeScript(new ResourceScriptSource(new InputStreamResource(is)));
    }

}
