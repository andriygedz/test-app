package com.test.pamc.service;

import static org.hibernate.internal.util.StringHelper.isNotEmpty;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.transaction.Transactional;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import com.test.pamc.entity.EpaperEntity;
import com.test.pamc.repository.EpaperRepository;
import com.test.pamc.xmlobjs.DeviceInfo;
import com.test.pamc.xmlobjs.EpaperRequest;
import com.test.pamc.xmlobjs.ScreenInfo;

@Service
public class EpaperService {
    private final EpaperRepository epaperRepository;

    private final Unmarshaller unmarshaller;

    public EpaperService(EpaperRepository epaperRepository) throws JAXBException, FileNotFoundException, SAXException {
        this.epaperRepository = epaperRepository;
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = sf.newSchema(ResourceUtils.getFile("classpath:xsd/epaperRequest.xsd"));
        JAXBContext jc = JAXBContext.newInstance(EpaperRequest.class);
        unmarshaller = jc.createUnmarshaller();
        unmarshaller.setSchema(schema);
    }

    @Transactional
    public void createEpaper(MultipartFile file) throws JAXBException, IOException {
        EpaperRequest epaperRequest = (EpaperRequest) unmarshaller.unmarshal(file.getInputStream());

        DeviceInfo deviceInfo = epaperRequest.getDeviceInfo();
        ScreenInfo screenInfo = deviceInfo.getScreenInfo();

        EpaperEntity epaperEntity = new EpaperEntity();
        epaperEntity.setFileName(file.getOriginalFilename());
        epaperEntity.setNewspaperName(deviceInfo.getAppInfo().getNewspaperName());
        epaperEntity.setWidth(screenInfo.getWidth());
        epaperEntity.setHeight(screenInfo.getHeight());
        epaperEntity.setDpi(screenInfo.getDpi());

        epaperRepository.save(epaperEntity);
    }

    public Page<EpaperEntity> findByNewspaperName(String newspaperName, Pageable pageable) {
        if (isNotEmpty(newspaperName)) {
            return epaperRepository.findAllByNewspaperName(newspaperName, pageable);
        }
        return epaperRepository.findAll(pageable);
    }
}
