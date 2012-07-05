using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSharpBenchmark.infrastructure.database.postgresql
{
    //Functional requirement 2.5
    //Test case 13: Not all files/classes have prefix �POST_� in package infrastructure.database.postgresql
    //Result: FALSE

    //Functional requirement 2.6
    //Test case 27: All classes in package infrastructure.database.postgresql must extend class infrastructure.database.Database
    //Result: TRUE
    public abstract class PostgreSqlConnectionSettings : Database
    {

    }
}