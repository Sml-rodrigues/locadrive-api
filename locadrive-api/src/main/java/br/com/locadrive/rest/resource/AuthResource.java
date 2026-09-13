package br.com.locadrive.rest.resource;

import io.smallrye.jwt.build.Jwt;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashSet;
import java.util.List;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @POST
    @Path("/login")
    @PermitAll
    public Response login(DTOAuthRequest request) {
        if ("admin@locadrive.com.br".equals(request.email) && "admin123".equals(request.senha)) {

            // O SmallRye assina automaticamente usando a chave definida no application.properties (privateKey.pem)
            String token = Jwt.issuer("https://locadrive.com.br")
                    .upn(request.email)
                    .groups(new HashSet<>(List.of("ADMIN", "USER")))
                    .expiresIn(3600)
                    .sign();

            return Response.ok(new DTOAuthResponse(token)).build();
        }

        return Response.status(Response.Status.UNAUTHORIZED)
                .entity("Credenciais inválidas.")
                .build();
    }

    public static class DTOAuthRequest {
        public String email;
        public String senha;
    }

    public static class DTOAuthResponse {
        public String token;

        public DTOAuthResponse(String token) {
            this.token = token;
        }
    }
}