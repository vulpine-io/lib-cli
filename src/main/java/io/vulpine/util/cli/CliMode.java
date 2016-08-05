package io.vulpine.util.cli;

import io.vulpine.util.cli.def.CliArgumentDef;
import io.vulpine.util.cli.def.CliModeDef;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public abstract class CliMode extends CliBase implements CliModeDef
{
  protected final Queue < String >       parameters;

  protected final ArgumentSet            arguments;

  protected final Map < String, String > values;

  public CliMode( final String n, final String d )
  {
    super(n, d);

    parameters = new LinkedList < String >();
    arguments = new ArgumentSet();
    values = new HashMap < String, String >();
  }

  @Override
  public CliMode addParameter( final String p )
  {
    parameters.offer(p);

    return this;
  }

  @Override
  public CliMode addArgument( final CliArgumentDef a )
  {
    arguments.addArgument(a);

    return this;
  }

  @Override
  public boolean hasUnfilledParam()
  {
    return null != parameters.peek();
  }

  @Override
  public CliMode parseParam( final String s )
  {
    final String next = parameters.poll();
    values.put(next, s);
    return this;
  }

  @Override
  public ArgumentSet getArgumentSet()
  {
    return arguments;
  }

  @Override
  public String getHelpText()
  {
    final String ls    = System.getProperty("line.separator");
    final String indls = ls + "    ";

    final StringBuilder sb = new StringBuilder(this.name);

    for ( final String param : parameters ) {
      sb.append(" [").append(param).append(']');
    }

    return sb
      .append(indls)
      .append(arguments.getHelpText().replaceAll(ls, indls))
      .toString();
  }
}
