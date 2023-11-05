package com.wanted.restaurant.boundedContext.sigungu.service;

import com.wanted.restaurant.base.rsData.RsData;
import com.wanted.restaurant.boundedContext.sigungu.entity.Sigungu;
import com.wanted.restaurant.boundedContext.sigungu.repository.SigunguRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SigunguService {

    private final SigunguRepository sigunguRepository;

    @Transactional
    public RsData initSigunguData() {

        // csv 파일 경로
        String fileLocation = "csv/sgg_lat_lon.csv";
        Path path = Paths.get(fileLocation);
        URI uri = path.toUri();

        // 데이터를 저장할 list
        List<Sigungu> sigungusList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new UrlResource(uri).getInputStream()))
        ) {
            String line = br.readLine();

            // 파일을 한 줄 씩 읽기
            while ((line = br.readLine()) != null) {
                String[] splits = line.split(",");
                Sigungu sigungu = new Sigungu(splits[0], splits[1],
                        Double.parseDouble(splits[2]), Double.parseDouble(splits[3]));
                sigungusList.add(sigungu);

            }

            // 중복 저장 방지
            sigunguRepository.deleteAll();

            // 데이터 저장
            sigunguRepository.saveAll(sigungusList);

        } catch (IOException e) {

            e.printStackTrace();
            return RsData.of("F-1", e.toString());
        }

        return RsData.of("S-1", "Data insertion complete", sigungusList);
    }

}