/*
 * Copyright 2021 Aleksandra Mišić .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.rwthaachen.wzl.gt.nbm.nbhelp.renderer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import de.rwthaachen.wzl.gt.nbm.nbhelp.api.HelpRenderContext;
import de.rwthaachen.wzl.gt.nbm.nbhelp.api.HelpTemplate;
import java.awt.Desktop;
import java.net.URISyntaxException;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;

import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Aleksandra Mišić
 */
//@ServiceProvider(service = HelpTemplate.class,
//        supersedes = "de.rwthaachen.wzl.gt.nbm.nbhelp.renderer.SimpleHelpRenderer")
public class NewRenderer implements HelpTemplate {

    @Override
    public long renderHelpPage(HelpRenderContext context) throws IOException {

        return System.currentTimeMillis();
    }

}
