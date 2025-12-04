package com.example.algoworld.config

import com.example.algoworld.domain.Area
import com.example.algoworld.domain.Stage
import com.example.algoworld.repository.AreaRepository
import com.example.algoworld.repository.StageRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.Transactional

@Configuration
class DataInitializer {
    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    fun loadSampleData(areaRepository: AreaRepository, stageRepository: StageRepository) = CommandLineRunner {
        if (areaRepository.count() > 0 || stageRepository.count() > 0) {
            log.info("Skipping seed data, existing records found")
            return@CommandLineRunner
        }

        val arrayVillage = areaRepository.save(
            Area(name = "Array Village", description = "배열 기초 스테이지", orderIndex = 1, posX = 10, posY = 20)
        )
        val sortHarbor = areaRepository.save(
            Area(name = "Sorting Harbor", description = "정렬 알고리즘", orderIndex = 2, posX = 40, posY = 35)
        )
        val dpMountain = areaRepository.save(
            Area(name = "DP Mountain", description = "동적계획법 입문", orderIndex = 3, posX = 70, posY = 60)
        )

        val stages = listOf(
            Stage(
                area = arrayVillage,
                name = "배열 합 구하기",
                description = "N개의 정수를 입력받아 총합을 구하세요.",
                difficulty = 1,
                unlockConditionStageId = null,
                problemType = "ARRAY",
                inputSpec = "N (1<=N<=10^5) 이후 N개의 정수",
                outputSpec = "정수 한 개 (합)",
                exampleInput = "5\\n1 2 3 4 5",
                exampleOutput = "15",
                posX = 12,
                posY = 24
            ),
            Stage(
                area = arrayVillage,
                name = "두 포인터 기초",
                description = "정렬된 배열에서 합이 X가 되는 두 수를 찾으세요.",
                difficulty = 2,
                unlockConditionStageId = null,
                problemType = "TWO_POINTER",
                inputSpec = "N, X 이후 오름차순 정렬된 N개의 정수",
                outputSpec = "조건을 만족하는 두 수의 인덱스",
                exampleInput = "6 9\\n1 2 3 5 7 11",
                exampleOutput = "2 5",
                posX = 16,
                posY = 26
            ),
            Stage(
                area = sortHarbor,
                name = "버블 정렬 시뮬레이션",
                description = "버블 정렬 과정을 출력하세요.",
                difficulty = 2,
                unlockConditionStageId = 2,
                problemType = "SORT",
                inputSpec = "N 이후 N개의 정수",
                outputSpec = "교환 과정 및 최종 결과",
                exampleInput = "5\\n4 3 2 1 5",
                exampleOutput = "(1,2) (2,3) ...",
                posX = 42,
                posY = 38
            ),
            Stage(
                area = sortHarbor,
                name = "퀵 정렬 파티션",
                description = "피벗을 기준으로 파티션한 결과를 반환하세요.",
                difficulty = 3,
                unlockConditionStageId = 3,
                problemType = "SORT",
                inputSpec = "N 이후 N개의 정수",
                outputSpec = "파티션된 배열",
                exampleInput = "6\\n9 3 7 1 4 2",
                exampleOutput = "3 1 2 | 4 | 9 7",
                posX = 48,
                posY = 42
            ),
            Stage(
                area = dpMountain,
                name = "계단 오르기",
                description = "한 번에 1칸 또는 2칸 오를 때 계단 오르는 방법 수를 구하세요.",
                difficulty = 2,
                unlockConditionStageId = 4,
                problemType = "DP",
                inputSpec = "계단 수 N",
                outputSpec = "방법의 수",
                exampleInput = "5",
                exampleOutput = "8",
                posX = 72,
                posY = 65
            ),
            Stage(
                area = dpMountain,
                name = "최장 증가 부분 수열",
                description = "주어진 수열의 LIS 길이를 구하세요.",
                difficulty = 4,
                unlockConditionStageId = 5,
                problemType = "DP",
                inputSpec = "N 이후 N개의 정수",
                outputSpec = "LIS 길이",
                exampleInput = "8\\n10 20 10 30 20 50 40 80",
                exampleOutput = "5",
                posX = 78,
                posY = 72
            )
        )

        stageRepository.saveAll(stages)
        log.info("Seed data inserted: {} areas, {} stages", areaRepository.count(), stageRepository.count())
    }
}
