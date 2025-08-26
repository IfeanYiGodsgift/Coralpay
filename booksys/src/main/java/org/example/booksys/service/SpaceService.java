package org.example.booksys.service;

import org.example.booksys.model.Space;
import org.example.booksys.repository.SpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpaceService {

    @Autowired
    private SpaceRepository spaceRepository;

    public Space createSpace(Space space) {
        return spaceRepository.save(space);
    }

    public List<Space> getAllSpaces() {
        return spaceRepository.findAll();
    }

    public Optional<Space> getSpaceById(int id) {
        return spaceRepository.findById(id);
    }

    public Space updateSpace(int id, Space updatedSpace) {
        return spaceRepository.findById(id).map(space -> {
            space.setName(updatedSpace.getName());
            space.setType(updatedSpace.getType());
            space.setCapacity(updatedSpace.getCapacity());
            space.setLocation(updatedSpace.getLocation());
            return spaceRepository.save(space);
        }).orElse(null);
    }

    public boolean deleteSpace(int id) {
        if (spaceRepository.existsById(id)) {
            spaceRepository.deleteById(id);
            return true;
        }
        return false;
    }
}