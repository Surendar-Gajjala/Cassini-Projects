def templateString = '''\
    <%
        names.each({name ->
    %>
    Hello $name
    <%})%>
'''

def binding = ["names": ["John", "Clara"]]

def engine = new groovy.text.SimpleTemplateEngine();
def template = engine.createTemplate(templateString).make(binding);

print template.toString()