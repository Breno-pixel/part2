public  abstract class GenericDAOT<T extends Persistente> implements IGenericDAO<T> {


    private SingletonMap singletonMap;

    public abstract Class<T> getTipoClasse();

    public abstract void atualiarDados(T entity, T entityCadastrado);

    public GenericDAO() {
        this.singletonMap = SingletonMap.getInstance();
    }

    public Long getChave(T entity) throws TipoChaveNaoEncontradaException {
        Field[] fields = entity.getClass().getDeclaredFields();
        Long returnValue = null;
        for (Field field : fields) {
            if (field.isAnnotationPresent(TipoChave.class)) {
                TipoChave tipoChave = field.getAnnotation(TipoChave.class);
                String nomeMetodo = tipoChave.value();
                try {
                    Method method = entity.getClass().getMethod(nomeMetodo);
                    returnValue = (Long) method.invoke(entity);
                    return returnValue;
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    //Criar exception de negócio TipoChaveNaoEncontradaException
                    e.printStackTrace();
                    throw new TipoChaveNaoEncontradaException("Não encontrado o atributo da chave " + nomeMetodo);
                }
            }
        }
        return null;
    }

    @Override
    public T buscarPorId(Long id) throws TipoChaveNaoEncontradaException {
        return singletonMap.get(id);
    }

    @Override
    public List<T> buscarTodos() {
        List<T> lista = new ArrayList<>(singletonMap.values());
        return lista;
    }

    @Override
    public boolean salvar(T entity) throws TipoChaveNaoEncontradaException {
        Long chave = get
