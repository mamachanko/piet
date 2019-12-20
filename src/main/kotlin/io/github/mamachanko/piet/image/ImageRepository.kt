package io.github.mamachanko.piet.image

import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.stereotype.Repository

@RepositoryRestResource
@Repository
interface ImageRepository : CrudRepository<Image, String>
