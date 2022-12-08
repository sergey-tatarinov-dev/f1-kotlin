package ru.project.f1.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import ru.project.f1.entity.GrandPrixResult
import ru.project.f1.entity.GrandPrixResultPerDriver
import ru.project.f1.entity.GrandPrixResultPerGrandPrix
import ru.project.f1.entity.Team
import ru.project.f1.repository.GrandPrixRepository
import ru.project.f1.repository.GrandPrixResultRepository
import ru.project.f1.service.GrandPrixResultService
import java.math.BigInteger
import java.util.*
import javax.persistence.EntityManager
import javax.transaction.Transactional

@Service
class GrandPrixResultServiceImpl : GrandPrixResultService {

    @Autowired
    lateinit var em: EntityManager

    @Autowired
    private lateinit var grandPrixRepository: GrandPrixRepository

    @Autowired
    private lateinit var grandPrixResultRepository: GrandPrixResultRepository

    override fun save(grandPrixResult: GrandPrixResult): GrandPrixResult =
        grandPrixResultRepository.save(grandPrixResult)

    override fun findById(id: BigInteger): Optional<GrandPrixResult> = grandPrixResultRepository.findById(id)

    override fun deleteById(id: BigInteger) = grandPrixResultRepository.deleteById(id)

    override fun findAll(pageable: Pageable): Page<GrandPrixResult> = grandPrixResultRepository.findAll(pageable)

    override fun findDistinctByGrandPrixId(): List<BigInteger> = grandPrixResultRepository.findDistinctByGrandPrixId()

    @Transactional
    override fun createViews() {
        val years = grandPrixRepository.findAllYears()
        years.forEach { year ->
            val grandPrix = grandPrixRepository.findAllGrandPrixByYear(year)
            val driverSubQuery = grandPrix.joinToString {
                "\n (select points " +
                " from grand_prix inner join grand_prix_result gpr on grand_prix.id = gpr.grand_prix_id " +
                " where id = ${it.id} and d.id = gpr.driver_id)\\:\\:text".trimIndent()
            }
            val teamSubQuery = grandPrix.joinToString {
                "\n (SELECT sum(points) " +
                " FROM grand_prix_result gpr " +
                " INNER JOIN line_up lu ON gpr.driver_id = lu.driver_id " +
                " INNER JOIN team ON team.id = lu.team_id " +
                " WHERE gpr.grand_prix_id = ${it.id} AND team.id = t.id)\\:\\:text".trimIndent()
            }
            val sql = "CREATE OR REPLACE FUNCTION create_views_$year() RETURNS TRIGGER AS ${'$'}${'$'} " +
                " DECLARE " +
                " BEGIN " +
                    " IF (TG_OP = 'INSERT' OR TG_OP = 'UPDATE') AND get_year_by_grand_prix_id(NEW.grand_prix_id) = $year THEN " +
                        " BEGIN " +
                            " CREATE OR REPLACE VIEW driver_standings_$year as " +
                            " SELECT id, name || ' ' || surname as name, " +
                            "       sum(points) as sum, " +
                            " json_build_array( " +
                            "       $driverSubQuery " +
                            " ) as points " +
                            " FROM driver d INNER JOIN grand_prix_result g ON d.id = g.driver_id " +
                            " GROUP BY name, surname, id " +
                            " ORDER BY sum DESC; " +

                            " CREATE OR REPLACE VIEW team_standings_$year AS " +
                            " SELECT id, " +
                            " name, " +
                            " sum(points) as sum, " +
                            " json_build_array( " +
                            "       $teamSubQuery" +
                            " ) AS points " +
                            " FROM grand_prix_result gpr " +
                            " INNER JOIN line_up lu ON gpr.driver_id = lu.driver_id " +
                            " INNER JOIN team t ON t.id = lu.team_id " +
                            " GROUP BY t.id " +
                            " ORDER BY sum DESC; " +
                        " END; " +
                    " END IF; " +
                    " RETURN NULL; " +
                " END " +
                "${'$'}${'$'} LANGUAGE plpgsql; " +
                " DROP TRIGGER IF EXISTS create_views_at_update_result ON grand_prix_result; " +
                " CREATE TRIGGER create_views_at_update_result " +
                    "    AFTER INSERT OR UPDATE ON grand_prix_result" +
                    "    FOR ROW" +
                    "    WHEN (pg_trigger_depth() < 1) " +
                    " EXECUTE PROCEDURE create_views_$year();".trimIndent()

            em.createNativeQuery(sql).executeUpdate()
        }
    }

    override fun findAllByGrandPrixId(id: Int): List<GrandPrixResultPerGrandPrix> =
        grandPrixResultRepository.findAllGrandPrixResultByIdAndYear(id)

    override fun findAllByDriverId(id: Int): List<GrandPrixResultPerDriver> =
        grandPrixResultRepository.findAllByDriverId(id)

    override fun findTeamByDriverId(id: BigInteger): Team = grandPrixResultRepository.findTeamByDriverId(id)[0]

}