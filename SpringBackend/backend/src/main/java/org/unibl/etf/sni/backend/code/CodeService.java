package org.unibl.etf.sni.backend.code;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.sni.backend.exception.NotFoundException;
import org.unibl.etf.sni.backend.user.UserModel;

import java.util.Optional;
import java.util.Random;

@Service
public class CodeService {

    @Autowired
    private CodeRepository codeRepository;

    public String saveCodeToDB(UserModel user) {

        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for(int i=0; i<4; i++) {
            sb.append(random.nextInt(10));
        }

        Code code = new Code();
        code.setCode(sb.toString());
        code.setUserId(user.getId());

        codeRepository.save(code);

        return sb.toString();
    }

    public Boolean insertCode(Integer userId, String userCode) throws NotFoundException {
        Code code  = codeRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException());


        if(code.getCode().equals(userCode)) {
            codeRepository.delete(code);
            return true;
        }
        return false;
    }

    @Transactional
    public void deleteCode(Integer fitnessUserId) throws NotFoundException {
        Code code  = codeRepository.findByUserId(fitnessUserId)
                .orElseThrow(() -> new NotFoundException());

        codeRepository.delete(code);
    }

    public Code getById(Integer id)  {
        Optional<Code> code =  codeRepository.findByUserId(id);

        if(code.isPresent()) {
            return code.get();
        }

        return null;
    }

}
