package se.kth.tablespoon.server.rest.api;


import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.eclipse.jetty.server.Authentication.User;

public class TablespoonApplication extends Application<TablespoonConfiguration> {
  public static void main(String[] args) throws Exception {
    new TablespoonApplication().run(args);
  }
  
  
  @Override
  public String getName() {
    return "hello-world";
  }
  
  @Override
  public void initialize(Bootstrap<TablespoonConfiguration> bootstrap) {
    // Enable variable substitution with environment variables
    bootstrap.setConfigurationSourceProvider(
        new SubstitutingSourceProvider(
            bootstrap.getConfigurationSourceProvider(),
            new EnvironmentVariableSubstitutor(false)
        )
    );
    
    bootstrap.addCommand(new RenderCommand());
    bootstrap.addBundle(new AssetsBundle());
  }
  
  @Override
  public void run(TablespoonConfiguration configuration, Environment environment) {
    final PersonDAO dao = new PersonDAO(hibernateBundle.getSessionFactory());
    final Template template = configuration.buildTemplate();
    
    environment.healthChecks().register("template", new TemplateHealthCheck(template));
    environment.jersey().register(DateRequiredFeature.class);
    environment.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
        .setAuthenticator(new ExampleAuthenticator())
        .setAuthorizer(new ExampleAuthorizer())
        .setRealm("SUPER SECRET STUFF")
        .buildAuthFilter()));
    environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
    environment.jersey().register(RolesAllowedDynamicFeature.class);
    environment.jersey().register(new TablespoonResource(template));
    environment.jersey().register(new ViewResource());
    environment.jersey().register(new ProtectedResource());
    environment.jersey().register(new PeopleResource(dao));
    environment.jersey().register(new PersonResource(dao));
    environment.jersey().register(new FilteredResource());
  }
}
