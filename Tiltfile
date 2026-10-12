# build
custom_build(
    ref = 'accounting-authz',
    command = './gradlew bootBuildImage --imageName $EXPECTED_REF',
    deps = ['build.gradle', 'src']
)

# db
k8s_yaml(['k8s/postgres.yml'])
k8s_resource('accounting-authz-db', port_forwards=['5432:5432'])

# deploy app
k8s_yaml(['k8s/deployment.yml', 'k8s/service.yml'])

# manage
k8s_resource('accounting-authz', resource_deps=['accounting-authz-db'])
k8s_resource('accounting-authz', port_forwards=['8000'])