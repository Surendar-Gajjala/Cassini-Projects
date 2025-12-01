package com.cassinisys.plm.scripting;

import com.cassinisys.platform.util.RandomString;
import com.cassinisys.platform.util.Utils;
import com.cassinisys.plm.BaseTest;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;

public class ScriptingTest extends BaseTest {
    @Autowired
    private ItemRevisionRepository revisionRepository;

    @Test
    public void testScriptingEngines() throws Exception {
        PLMItemRevision itemRevision = revisionRepository.findOne(24);
        if(itemRevision != null) {

            Binding bindings = new Binding();
            bindings.setVariable("item", itemRevision);

            ImportCustomizer importCustomizer = new ImportCustomizer();
            importCustomizer.addStarImports("com.cassinisys.plm.model.plm");
            CompilerConfiguration configuration = new CompilerConfiguration();
            configuration.addCompilationCustomizers(importCustomizer);

            GroovyShell gse = new GroovyShell(bindings, configuration);

            String exp = "item.lifeCyclePhase.phaseType == LifeCyclePhaseType.RELEASED";
            Object bindingsResult = gse.evaluate(exp);
            System.out.println(bindingsResult);
        }

    }

}
