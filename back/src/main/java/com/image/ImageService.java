package com.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    public List<Image> findAll() {
        return imageRepository.findAll();
    }

    public Optional<Image> findOne(long id) {
        return imageRepository.findById(id);
    }

    public void save(Image theme) {
        imageRepository.save(theme);
    }

    public void delete(long id) {
        imageRepository.deleteById(id);
    }

    @Transactional
    public void setImage(Image image, MultipartFile file) {
        try {
            Byte[] byteObjects = new Byte[file.getBytes().length];
            int i = 0;
            for (byte b : file.getBytes()) {
                byteObjects[i++] = b;
            }
            image.setImage(byteObjects);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
