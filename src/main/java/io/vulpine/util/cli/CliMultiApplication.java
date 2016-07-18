package io.vulpine.util.cli;

import io.vulpine.util.cli.def.CliApplicationDef;
import io.vulpine.util.cli.def.CliArgumentDef;
import io.vulpine.util.cli.def.CliModeDef;
import io.vulpine.util.cli.def.CliParameterDef;

import java.util.*;
import java.util.Map.Entry;

public class CliMultiApplication implements CliApplicationDef
{
  protected final ArgumentSet arguments;
  protected final Queue < CliParameterDef > parameters;
  protected final ArgumentParser parser;
  protected final Map < String, CliModeDef > modes;


  public CliMultiApplication ( final String[] args )
  {
    arguments  = new ArgumentSet();
    parser     = new ArgumentParser(args, true);
    parameters = new LinkedList < CliParameterDef >();
    modes      = new HashMap < String, CliModeDef >();
  }

  @Override
  public CliMultiApplication addArgument ( final CliArgumentDef a )
  {
    arguments.addArgument(a);

    return this;
  }

  public CliMultiApplication addAppMode ( final CliModeDef c )
  {
    modes.put(c.getName(), c);

    return this;
  }

  @Override
  public CliMultiApplication addParameter ( final CliParameterDef def )
  {
    parameters.offer(def);

    return this;
  }

  @Override
  public void run ()
  {
    final CliModeDef mode;
    final Queue< String > params;
    final Iterator < Entry < String, List < String > > > nvIt;
    final Iterator < Entry < Character, List < String > > > kvIt;
    final Iterator < String > nfIt;
    final Iterator < Character > kfIt;

    mode = modes.get(parser.getCliMode());

    if (mode == null) throw new RuntimeException("Unrecognized application mode.");

    nvIt = parser.getArgumentsByName().entrySet().iterator();
    while ( nvIt.hasNext() ) {
      final Entry < String, List < String > > e = nvIt.next();
      testArg(e.getKey(), e.getValue(), mode);
    }

    kvIt = parser.getArgumentsByKey().entrySet().iterator();
    while ( kvIt.hasNext() ) {
      final Entry < Character, List < String > > e = kvIt.next();
      testArg(e.getKey(), e.getValue(), mode);
    }

    nfIt = parser.getFlagsByName().iterator();
    while ( nfIt.hasNext() )
      testArg(nfIt.next(), mode);

    kfIt = parser.getFlagsByKey().iterator();
    while ( kfIt.hasNext() )
      testArg(kfIt.next(), mode);

    params = parser.getParameters();

    while (!params.isEmpty())
      mode.addParameter(params.poll());

    for ( final CliArgumentDef a : arguments.getArguments() )
      if (a.isRequired() && !a.wasUsed())
        throw new RuntimeException(String.format("Argument %s|%s is required.", a.getKey(), a.getName()));

    for ( final CliArgumentDef a : mode.getArgumentSet().getArguments() )
      if (a.isRequired() && !a.wasUsed())
        throw new RuntimeException(String.format("Argument %s|%s is required.", a.getKey(), a.getName()));

    mode.run(parser);
  }

  private void testArg (final String e, final CliModeDef mode )
  {
    final CliArgumentDef a = arguments.getArgument(e);
    testFlag(null == a ? mode.getArgumentSet().getArgument(e) : a, e);
  }

  private void testArg (final char e, final CliModeDef mode )
  {
    final CliArgumentDef a = arguments.getArgument(e);
    testFlag(null == a ? mode.getArgumentSet().getArgument(e) : a, e);
  }

  private void testArg ( final String name, final List < String > values, final CliModeDef mode )
  {
    final CliArgumentDef a = arguments.getArgument(name);
    nullCheckArg(null == a ? mode.getArgumentSet().getArgument(name) : a, name, values);
  }

  private void testArg ( final char key, final List < String > values, final CliModeDef mode )
  {
    final CliArgumentDef a = arguments.getArgument(key);
    nullCheckArg(null == a ? mode.getArgumentSet().getArgument(key) : a, key, values);
  }

  private void testFlag ( final CliArgumentDef a, final Object e )
  {
    assert null != a : "Unrecognized flag " + e;
    assert !a.getParameter().isRequired() : String.format("Argument --%s requires a value.", e);
    a.use();
  }

  private void nullCheckArg ( final CliArgumentDef a, final Object i, final List < String > v )
  {
    assert null != a : "Unrecognized Argument " + i;
    insertValues(a, v);
  }

  private void insertValues( final CliArgumentDef a, final List < String > values )
  {
    for ( final String s : values ) a.parseParam(s);
    a.use();
  }
}
