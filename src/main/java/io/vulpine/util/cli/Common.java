/*
 * Copyright 2016 Elizabeth Harper
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vulpine.util.cli;

import io.vulpine.util.cli.def.HasDescription;
import io.vulpine.util.cli.def.HasName;

class Common implements HasName, HasDescription
{
  protected final String name, description;

  Common ( String name, String description )
  {
    this.name = name;
    this.description = description;
  }

  public String getDescription ()
  {
    return description;
  }

  public String getName ()
  {
    return name;
  }
}
