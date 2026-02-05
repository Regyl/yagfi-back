package com.github.regyl.gfi.service.impl.feed.cyclonedx.homepage.maven;

import com.github.packageurl.PackageURL;
import com.github.regyl.gfi.service.feed.PurlToBuildFileService;
import com.github.regyl.gfi.service.feed.PurlToHomepageService;
import com.github.regyl.gfi.util.LinkUtil;
import com.github.regyl.gfi.util.ServicePredicateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class MavenPurlToHomepageServiceImpl implements PurlToHomepageService {

    private final Collection<PurlToBuildFileService> buildFileServices;

    @Override
    public boolean test(PackageURL purl) {
        return purl.getType().equals("maven");
    }

    @Override
    public String apply(PackageURL purl) {
        if (purl == null) {
            return null;
        }

        PurlToBuildFileService buildFileService = ServicePredicateUtil.getTargetService(buildFileServices, purl);
        String buildFile = buildFileService.apply(purl);
        if (buildFile == null) {
            return null;
        }

        String repoUrl = extractScmUrl(buildFile);
        return LinkUtil.normalizeRepositoryUrl(repoUrl);
    }

    private String extractScmUrl(String buildFile) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);
            factory.setValidating(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(buildFile)));

            NodeList scmNodes = doc.getElementsByTagName("scm");
            if (scmNodes.getLength() == 0) {
                return null;
            }

            Element scmElement = (Element) scmNodes.item(0);
            NodeList urlNodes = scmElement.getElementsByTagName("url");
            if (urlNodes.getLength() == 0) {
                return null;
            }

            Node urlNode = urlNodes.item(0);
            return urlNode.getTextContent().trim();
        } catch (Exception e) {
            log.warn("Failed to parse POM XML", e);
            return null;
        }
    }
}
